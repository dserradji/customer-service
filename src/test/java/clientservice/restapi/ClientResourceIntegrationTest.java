package clientservice.restapi;

import static clientservice.domain.enums.ClientType.COMPANY;
import static clientservice.domain.enums.ClientType.PERSON;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

import clientservice.domain.Client;
import clientservice.repository.mongodb.ClientRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
public class ClientResourceIntegrationTest {

	@MockBean
	private ClientRepository repo;

	private WebTestClient webClient;
	
	@Before
	public void init() {
		webClient = WebTestClient.bindToController(new ClientResource(repo)).build();
	}

	@Test
	public void shouldReturnAllClients() {

		final List<Client> mockClients = asList(Client.ofType(PERSON).build(), Client.ofType(COMPANY).build());
		given(repo.findAll()).willReturn(Flux.fromIterable(mockClients));

		webClient.get().uri("/clients").accept(APPLICATION_JSON_UTF8).exchange()
			.expectStatus().isOk()	// HTTP 200
			.expectHeader().contentType(APPLICATION_JSON_UTF8)
			.expectBodyList(Client.class).hasSize(2).consumeWith(clients -> {
					assertThat(clients.stream().map(Client::getClientType).collect(toList())
							.containsAll(asList(PERSON, COMPANY)));
				});
	}
	
	@Test
	public void shouldReturnEmptyBodyWhenNoClientsFound() {

		final List<Client> clients = Collections.emptyList();
		given(repo.findAll()).willReturn(Flux.fromIterable(clients));

		
		webClient.get().uri("/clients").accept(APPLICATION_JSON_UTF8).exchange()
			.expectStatus().isNoContent();	// HTTP 204
	}
	
	@Test
	public void shouldReturnOneClientById() throws Exception {

		final LocalDate birthDate = LocalDate.of(1990, Month.JULY, 31);
		final Client mockClient = Client.ofType(PERSON).withBirthDate(birthDate).build();
		final ObjectId id = ObjectId.get();

		given(repo.findOne(any(ObjectId.class))).willReturn(Mono.just(mockClient));

		webClient.get().uri(String.format("/clients/%s", id)).accept(APPLICATION_JSON_UTF8).exchange()
			.expectStatus().isOk()	// HTTP 200
			.expectBody(Client.class)
			.consumeWith(client -> {
					assertThat(client.getClientType()).isEqualTo(PERSON);
					assertThat(client.getBirthDate()).isEqualTo(LocalDate.of(1990, 07, 31));
				});
	}

	@Test
	public void shouldReturn404IfClientNotFound() throws Exception {

		given(repo.findOne(any(ObjectId.class))).willReturn(Mono.empty());

		webClient.get().uri(String.format("/clients/%s", ObjectId.get())).accept(APPLICATION_JSON_UTF8).exchange()
				.expectStatus().isNotFound() // HTTP 404
				.expectBody().isEmpty();
	}

	@Test
	public void shouldAddANewClient() throws Exception {

		final Client newClient = Client.ofType(PERSON).build();

		final ObjectId id = ObjectId.get();
		ReflectionTestUtils.setField(newClient, "id", id);

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(false));
		given(repo.save(any(Client.class))).willReturn(Mono.just(newClient));

		webClient.post().uri("/clients").contentType(APPLICATION_JSON_UTF8).body("{\"client_type\":\"PERSON\"}").exchange()
			.expectStatus().isCreated()	// HTTP 201
			.expectHeader().valueEquals("Location", String.format("/clients/%s", id));
	}

	@Test
	public void shouldNotAddClientIfContentIsNotValid() throws Exception {

		final String BAD_JSON = "{\"client_type_is_missing\":\"PERSON\"}";

		webClient.post().uri("/clients").contentType(APPLICATION_JSON_UTF8).body(BAD_JSON).exchange().expectStatus()
				.isBadRequest();	// HTTP 400
	}

	@Test
	public void shouldNotAddClientIfClientAlreadyExists() throws Exception {

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(true));
		final ObjectId id = ObjectId.get();

		final String EXISTING_CLIENT = String.format("{\"id\":\"%s\",\"client_type\":\"COMPANY\"}", id);
		webClient.post().uri("/clients").contentType(APPLICATION_JSON_UTF8).body(EXISTING_CLIENT).exchange()
				.expectStatus().is5xxServerError(); // HTTP 500 because the
													// exception handler is not
													// used yet.
	}

	@Test
	public void shouldUpdateAnExistingClient() throws Exception {

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(true));
		given(repo.save(any(Client.class))).willReturn(Mono.just(Client.ofType(PERSON).build()));

		final ObjectId id = ObjectId.get();
		final String UPDATE = String.format(
				"{\"id\":\"%s\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"client_type\":\"COMPANY\"}", id);

		webClient.put().uri(String.format("/clients/%s", id)).contentType(APPLICATION_JSON_UTF8).body(UPDATE).exchange()
				.expectStatus().isNoContent(); // HTTP 204
	}

	@Test
	public void shouldFailUpdatingNonExistingClient() throws Exception {

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(false));

		final ObjectId id = ObjectId.get();
		final String UPDATE = String.format(
				"{\"id\":\"%s\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"client_type\":\"COMPANY\"}", id);

		webClient.put().uri(String.format("/clients/%s", id)).contentType(APPLICATION_JSON_UTF8).body(UPDATE).exchange()
				.expectStatus().is5xxServerError(); // HTTP 500 because the
													// exception handler is not
													// used yet.
	}

	@Test
	public void shouldDeleteAnExistingClient() throws Exception {

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(true));
		given(repo.delete(any(ObjectId.class))).willReturn(Mono.empty());
		final URI uri = URI.create(String.format("/clients/%s", ObjectId.get()));
		
		webClient.delete().uri(uri).exchange().expectStatus().isNoContent();
	}

	@Test
	public void shouldDeleteExistingClientAndIgnoreFollowingCalls() throws Exception {

		given(repo.exists(any(ObjectId.class))).willReturn(Mono.just(true)).willReturn(Mono.just(false));
		given(repo.delete(any(ObjectId.class))).willReturn(Mono.empty());
		final URI uri = URI.create(String.format("/clients/%s", ObjectId.get()));

		// Expect HTTP 204 for each call
		webClient.delete().uri(uri).exchange().expectStatus().isNoContent();
		webClient.delete().uri(uri).exchange().expectStatus().isNoContent();
		webClient.delete().uri(uri).exchange().expectStatus().isNoContent();
	}
}
