package clientservice.restapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@RequestMapping(method = GET)
	public ResponseEntity<?> allClients() {

		final Iterable<Client> it = repo.findAll();
		return it.iterator().hasNext() ? ResponseEntity.ok(it) : ResponseEntity.noContent().build();
	}

	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> addClient(@RequestBody @Valid Client newClient) {

		if (newClient.getId() != null && repo.exists(newClient.getId())) {
			throw new ClientResourceException(HttpStatus.BAD_REQUEST, "Client already exists.");
		}

		final Client created = repo.save(newClient);
		return ResponseEntity.created(URI.create(String.format("/clients/%s", created.getId()))).build();
	}

	// @RequestMapping(method = PUT, consumes = { APPLICATION_JSON_UTF8_VALUE })
	// public ResponseEntity<?> updateClient(@RequestBody @Valid Client update)
	// {
	//
	// final Client updatedClient = repo.save(update);
	// return ResponseEntity.created(URI.create(String.format("/clients/%s",
	// created.getId()))).build();
	// }
}
