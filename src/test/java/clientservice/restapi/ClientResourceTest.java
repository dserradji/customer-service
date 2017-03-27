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
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import clientservice.models.Client;
import clientservice.models.enums.ClientType;
import clientservice.repositories.mongodb.ClientRepository;

@RunWith(MockitoJUnitRunner.class)
public class ClientResourceTest {

	@Mock
	private ClientRepository repo;

	@InjectMocks
	private ClientResource controller;

	@SuppressWarnings("unchecked")
	@Test
	public void shouldReturnAllClients() {

		// Given
		final List<Client> clients = asList(Client.ofType(PERSON).build(), Client.ofType(COMPANY).build());
		when(repo.findAll()).thenReturn(clients);

		// When
		final ResponseEntity<?> response = controller.allClients();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat((Iterable<Client>) response.getBody()).asList().containsAll(clients);
	}

	@Test
	public void shouldReturnEmptyBodyWhenNoClients() {

		// Given
		when(repo.findAll()).thenReturn(Collections.emptyList());

		// When
		final ResponseEntity<?> response = controller.allClients();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldAddANewClient() {

		// Given
		final Client newClient = Client.ofType(PERSON).build();

		final ObjectId id = ObjectId.get();
		ReflectionTestUtils.setField(newClient, "id", id);

		when(repo.save(any(Client.class))).thenReturn(newClient);

		// When
		final ResponseEntity<?> response = controller.addClient(newClient);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getHeaders().getLocation().toString()).isEqualTo(format("/clients/%s", id));
	}

	@Test
	public void shouldNotAddClientIfClientAlreadyExists() throws Exception {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(true);
		final ObjectId id = ObjectId.get();
		final Client client = Client.ofType(PERSON).build();
		ReflectionTestUtils.setField(client, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.addClient(client)).isInstanceOf(ClientResourceException.class)
				.hasMessageContaining("Client already exists");
	}

	@Test
	public void shouldUpdateAnExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(true);
		when(repo.save(any(Client.class))).thenReturn(Client.ofType(PERSON).build());
		final ObjectId id = ObjectId.get();
		final Client existingClient = Client.ofType(ClientType.PERSON).build();
		ReflectionTestUtils.setField(existingClient, "id", id);

		// When
		final ResponseEntity<?> response = controller.updateClient(existingClient.getId(), existingClient);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldFailUpdatingNonExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(false);
		final ObjectId id = ObjectId.get();
		final Client newClient = Client.ofType(ClientType.PERSON).build();
		ReflectionTestUtils.setField(newClient, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.updateClient(newClient.getId(), newClient))
				.isInstanceOf(ClientResourceException.class).hasMessageContaining("Client does not exist");
	}

	@Test
	public void shouldDeleteAnExistingClient() {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(true);
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response = controller.deleteClient(id);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldDeleteExistingClientAndIgnoreSubsequentCalls() throws Exception {

		// Given
		when(repo.exists(any(ObjectId.class))).thenReturn(true).thenReturn(false);
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response1 = controller.deleteClient(id);
		final ResponseEntity<?> response2 = controller.deleteClient(id);
		final ResponseEntity<?> response3 = controller.deleteClient(id);

		// Then
		assertThat(response1.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response2.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response3.getStatusCode()).isEqualTo(NO_CONTENT);
	}

}
