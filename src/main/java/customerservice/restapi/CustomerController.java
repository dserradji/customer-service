package customerservice.restapi;

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

import customerservice.CustomerServiceException;
import customerservice.domain.Customer;
import customerservice.repository.mongodb.CustomerRepository;

@RestController
@RequestMapping(path = "/customers", produces = { APPLICATION_JSON_UTF8_VALUE })
public class CustomerController {

	private CustomerRepository repo;

	public CustomerController(CustomerRepository repo) {
		this.repo = repo;
	}

	/**
	 * Query for all customers.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @return HTTP 200 if customers found or HTTP 204 otherwise.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
	@RequestMapping(method = GET)
	public ResponseEntity<?> allCustomers() {

		final Iterable<Customer> it = repo.findAll();
		return it.iterator().hasNext() ? ok(it) : noContent().build();
	}

	/**
	 * Query for a customer with the given Id.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @param id
	 *            The id of the customer to look for.
	 * 
	 * @return HTTP 200 if the customer is found or HTTP 404 otherwise.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> oneCustomer(@PathVariable @NotNull ObjectId id) {

		return repo.findById(id).map(customer -> ok(customer)).orElse(notFound().build());
	}

	/**
	 * Create a new customer.
	 * 
	 * @param newCustomer
	 *            The customer to create.
	 * 
	 * @return HTTP 201, the header Location contains the URL of the created
	 *         customer.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> addCustomer(@RequestBody @Valid Customer newCustomer) {

		if (newCustomer.getId() != null && repo.existsById(newCustomer.getId())) {
			throw new CustomerServiceException(HttpStatus.BAD_REQUEST,
					"Customer already exists, to update an existing customer use PUT instead.");
		}

		final Customer created = repo.save(newCustomer);
		return created(URI.create(String.format("/customers/%s", created.getId()))).build();
	}

	/**
	 * Update an existing customer.
	 * <p>
	 * This method is idempotent.
	 * <p>
	 * 
	 * @param id
	 *            The id of the customer to update.
	 * @param update
	 *            The Customer object containing the updated version to be
	 *            persisted.
	 * 
	 * @return HTTP 204 otherwise HTTP 400 if the customer does not exist.
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = PUT, value = "/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> updateCustomer(@PathVariable @NotNull ObjectId id, @RequestBody @Valid Customer update) {

		if (!repo.existsById(id)) {
			throw new CustomerServiceException(HttpStatus.BAD_REQUEST,
					"Customer does not exist, to create a new customer use POST instead.");
		}

		repo.save(update);
		return noContent().build();
	}

	/**
	 * Delete a customer.
	 * <p>
	 * This method is idempotent, if it's called multiples times with the same
	 * id then the first call will delete the customer and subsequent calls will
	 * be silently ignored.
	 * 
	 * @param id
	 *            The id of the customer to delete.
	 * @return HTTP 204
	 */
	@PreAuthorize("#oauth2.hasAnyScope('write','read-write')")
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable @NotNull ObjectId id) {

		if (repo.existsById(id)) {
			repo.deleteById(id);
		}

		return noContent().build();
	}
}
