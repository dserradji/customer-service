package clientservice.restapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clientservice.models.Client;
import clientservice.repositories.mongodb.ClientRepository;

@RestController
@RequestMapping(path = "/clients", produces = { APPLICATION_JSON_UTF8_VALUE })
public class ClientResource {

	private ClientRepository repo;

	public ClientResource(ClientRepository repo) {
		this.repo = repo;
	}

	/**
	 * Query for the list of all clients.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @return HTTP 200 and the body containing all clients or HTTP 204 with
	 *         empty body.
	 */
	@RequestMapping(method = GET)
	public ResponseEntity<?> allClients() {

		final Iterable<Client> it = repo.findAll();
		return it.iterator().hasNext() ? ok(it) : noContent().build();
	}

	/**
	 * Query for the client with the given Id.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @param id
	 *            The id of the client.
	 * 
	 * @return HTTP 200 and the body containing the client or 404 if the client
	 *         is not found
	 */
	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> oneClient(@PathVariable @NotNull ObjectId id) {

		return repo.findOne(id).map(client -> ok(client)).orElse(notFound().build());
	}

	/**
	 * Create a new client.
	 * 
	 * @param newClient The client to create.
	 * 
	 * @return HTTP 201 with empty body and the header Location containing the URL of the created client.
	 */
	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> addClient(@RequestBody @Valid Client newClient) {

		if (newClient.getId() != null && repo.exists(newClient.getId())) {
			throw new ClientResourceException(HttpStatus.BAD_REQUEST,
					"Client already exists, to update an existing client use PUT instead.");
		}

		final Client created = repo.save(newClient);
		return created(URI.create(String.format("/clients/%s", created.getId()))).build();
	}

	/**
	 * Update an existing client.
	 * <p>
	 * This method is idempotent.
	 * <p>
	 * 
	 * @param id
	 *            The id of the client to update.
	 * @param update
	 *            The Client object containing the updated version to be
	 *            persisted.
	 * 
	 * @return 204 with empty body or 400 if the client does not exist. 
	 */
	@RequestMapping(method = PUT, value = "/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> updateClient(@PathVariable @NotNull ObjectId id, @RequestBody @Valid Client update) {

		if (!repo.exists(id)) {
			throw new ClientResourceException(HttpStatus.BAD_REQUEST,
					"Client does not exist, to create a new client use POST instead.");
		}

		repo.save(update);
		return noContent().build();
	}

	/**
	 * Delete a client.
	 * <p>
	 * This method is idempotent, if it's called multiples times with the same
	 * id then the first call will delete the client and subsequent calls will
	 * be silently ignored.
	 * 
	 * @param id
	 *            The id of the client to delete.
	 * @return 204 with empty body.
	 */
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable @NotNull ObjectId id) {

		if (repo.exists(id)) {
			repo.delete(id);
		}

		return noContent().build();
	}
}
