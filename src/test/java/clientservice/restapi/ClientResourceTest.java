package clientservice.restapi;

import static clientservice.models.enums.ClientType.COMPANY;
import static clientservice.models.enums.ClientType.PERSON;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import clientservice.ClientServiceException;
import clientservice.models.Client;
import clientservice.models.enums.ClientType;
import clientservice.repositories.mongodb.ClientRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class ClientResourceTest {

	@Mock
	private ClientRepository repo;

	@InjectMocks
	private ClientResource controller;

	@Test
	public void shouldReturnAllClients() {

		// Given
		final List<Client> clients = asList(Client.ofType(PERSON).build(), Client.ofType(COMPANY).build());
		when(repo.findAll()).thenReturn(Flux.fromIterable(clients));

		// When
		final ResponseEntity<List<Client>> response = controller.allClients().block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat((Iterable<Client>) response.getBody()).asList().containsAll(clients);
	}

	@Test
	public void shouldReturnEmptyBodyWhenNoClients() {

		// Given
		when(repo.findAll()).thenReturn(Flux.empty());

		// When
		final ResponseEntity<List<Client>> response = controller.allClients().block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldReturnOneClientById() {

		// Given
		final Client client = Client.ofType(PERSON).build();
		when(repo.findOne(any(ObjectId.class))).thenReturn(Mono.just(client));

		// When
		final ResponseEntity<Client> response = controller.oneClient(ObjectId.get()).block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat((Client) response.getBody()).isEqualTo(client);
	}

	@Test
	public void shouldReturn404IfClientIsNotFound() {

		// Given
		when(repo.findOne(any(ObjectId.class))).thenReturn(Mono.empty());

		// When
		final ResponseEntity<Client> response = controller.oneClient(ObjectId.get()).block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}

	@Test
	public void shouldAddANewClient() {

		// Given
		final Client newClient = Client.ofType(PERSON).build();

		final ObjectId id = ObjectId.get();
		ReflectionTestUtils.setField(newClient, "id", id);

		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(false));
		when(repo.save(any(Client.class))).thenReturn(Mono.just(newClient));

		// When
		final ResponseEntity<?> response = controller.addClient(newClient).block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getHeaders().getLocation().toString()).isEqualTo(format("/clients/%s", id));
	}

	@Test
	public void shouldNotAddAClientIfClientAlreadyExists() throws Exception {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(true));
		final ObjectId id = ObjectId.get();
		final Client client = Client.ofType(PERSON).build();
		ReflectionTestUtils.setField(client, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.addClient(client).block())
				.isInstanceOf(ClientServiceException.class).hasMessageContaining("Client already exists");
	}

	@Test
	public void shouldUpdateAnExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(true));
		when(repo.save(any(Client.class))).thenReturn(Mono.just(Client.ofType(PERSON).build()));
		final ObjectId id = ObjectId.get();
		final Client existingClient = Client.ofType(ClientType.PERSON).build();
		ReflectionTestUtils.setField(existingClient, "id", id);

		// When
		final ResponseEntity<?> response = controller.updateClient(id, existingClient).block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldFailUpdatingNonExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(false));
		final ObjectId id = ObjectId.get();
		final Client newClient = Client.ofType(ClientType.PERSON).build();
		ReflectionTestUtils.setField(newClient, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.updateClient(newClient.getId(), newClient).block())
				.isInstanceOf(ClientServiceException.class).hasMessageContaining("Client does not exist");
	}

	@Test
	public void shouldDeleteAnExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(true));
		when(repo.delete(any(ObjectId.class))).thenReturn(Mono.empty());
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response = controller.deleteClient(id).block();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldDeleteExistingClientAndIgnoreSubsequentCalls() throws Exception {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(Mono.just(true)).thenReturn(Mono.just(false));
		when(repo.delete(any(ObjectId.class))).thenReturn(Mono.empty());
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response1 = controller.deleteClient(id).block();
		final ResponseEntity<?> response2 = controller.deleteClient(id).block();
		final ResponseEntity<?> response3 = controller.deleteClient(id).block();

		// Then
		assertThat(response1.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response2.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response3.getStatusCode()).isEqualTo(NO_CONTENT);
	}

}
