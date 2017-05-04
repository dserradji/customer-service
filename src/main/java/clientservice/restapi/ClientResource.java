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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clientservice.ClientServiceException;
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
	 * Query for all clients.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @return HTTP 200 if clients found or HTTP 204 otherwise.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
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
	 * @return HTTP 200 if the client is found or HTTP 404 otherwise.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> oneClient(@PathVariable @NotNull ObjectId id) {

		return repo.findById(id).map(client -> ok(client)).orElse(notFound().build());
	}

	/**
	 * Create a new client.
	 * 
	 * @param newClient
	 *            The client to create.
	 * 
	 * @return HTTP 201, the header Location contains the URL of the created
	 *         client.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> addClient(@RequestBody @Valid Client newClient) {

		if (newClient.getId() != null && repo.existsById(newClient.getId())) {
			throw new ClientServiceException(HttpStatus.BAD_REQUEST,
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
	 * @return HTTP 204 otherwise HTTP 400 if the client does not exist.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = PUT, value = "/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> updateClient(@PathVariable @NotNull ObjectId id, @RequestBody @Valid Client update) {

		if (!repo.existsById(id)) {
			throw new ClientServiceException(HttpStatus.BAD_REQUEST,
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
	 * @return HTTP 204
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable @NotNull ObjectId id) {

		if (repo.existsById(id)) {
			repo.deleteById(id);
		}

		return noContent().build();
	}
}
