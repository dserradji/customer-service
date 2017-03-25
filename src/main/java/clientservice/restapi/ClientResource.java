package clientservice.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import clientservice.models.Client;
import clientservice.repositories.mongodb.ClientRepository;

@RestController
@RequestMapping(path = "/clients", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class ClientResource {

	@Autowired
	private ClientRepository repo;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> allClients() {

		final Iterable<Client> it = repo.findAll();
		return it.iterator().hasNext() ? ResponseEntity.ok(it) : ResponseEntity.noContent().build();
	}
}
