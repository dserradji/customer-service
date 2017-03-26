package clientservice.restapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ClientRepository repo;

	/**
	 * Return all existing clients.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @return A list of clients
	 */
	@RequestMapping(method = GET)
	public ResponseEntity<?> allClients() {

		final Iterable<Client> it = repo.findAll();
		return it.iterator().hasNext() ? ResponseEntity.ok(it) : ResponseEntity.noContent().build();
	}

	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> addClient(@RequestBody @Valid Client newClient) {

		if (newClient.getId() != null && repo.exists(newClient.getId())) {
			throw new ClientResourceException(HttpStatus.BAD_REQUEST,
					"Client already exists, to update an existing client use PUT instead.");
		}

		final Client created = repo.save(newClient);
		return ResponseEntity.created(URI.create(String.format("/clients/%s", created.getId()))).build();
	}

	/**
	 * Updates an existing client.
	 * <p>
	 * This method is idempotent.
	 * <p>
	 * @param id The id of the client to update
	 * @param update The Client object containing the updated version to be persisted
	 * 
	 * @return
	 */
	@RequestMapping(method = PUT, value = "/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> updateClient(@PathVariable @NotNull ObjectId id, @RequestBody @Valid Client update) {

		if (!repo.exists(id)) {
			throw new ClientResourceException(HttpStatus.BAD_REQUEST,
					"Client does not exist, to create a new client use POST instead.");
		}

		repo.save(update);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Deletes a client identified by its id.
	 * <p>
	 * This method is idempotent, if it's called multiples times with the same
	 * id then the first call will delete the client and the following calls
	 * will be silently ignored.
	 * 
	 * @param id
	 *            The id of the client to delete
	 * @return
	 */
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable @NotNull ObjectId id) {

		if (repo.exists(id)) {
			repo.delete(id);
		}

		return ResponseEntity.noContent().build();
	}
}
