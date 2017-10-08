package customerservice.restapi;

import static customerservice.domain.enums.CustomerType.COMPANY;
import static customerservice.domain.enums.CustomerType.PERSON;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import customerservice.CustomerServiceException;
import customerservice.domain.Customer;
import customerservice.domain.enums.CustomerType;
import customerservice.repository.mongodb.CustomerRepository;
import customerservice.restapi.CustomerController;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

	@Mock
	private CustomerRepository repo;

	@InjectMocks
	private CustomerController controller;

	@SuppressWarnings("unchecked")
	@Test
	public void shouldReturnAllCustomers() {

		// Given
		final List<Customer> customers = asList(
				Customer.ofType(PERSON).build(), 
				Customer.ofType(COMPANY).build());
		when(repo.findAll()).thenReturn(customers);

		// When
		final ResponseEntity<?> response = controller.allCustomers();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat((Iterable<Customer>) response.getBody()).asList().containsAll(customers);
	}

	@Test
	public void shouldReturnEmptyBodyWhenNoCustomers() {

		// Given
		when(repo.findAll()).thenReturn(Collections.emptyList());

		// When
		final ResponseEntity<?> response = controller.allCustomers();

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldReturnOneCustomerById() {

		// Given
		final Customer customer = Customer.ofType(PERSON).build();
		when(repo.findById(any(ObjectId.class))).thenReturn(Optional.of(customer));

		// When
		final ResponseEntity<?> response = controller.oneCustomer(ObjectId.get());

		// Then
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat((Customer) response.getBody()).isEqualTo(customer);
	}

	@Test
	public void shouldReturn404IfCustomerIsNotFound() {

		// Given
		when(repo.findById(any(ObjectId.class))).thenReturn(Optional.empty());

		// When
		final ResponseEntity<?> response = controller.oneCustomer(ObjectId.get());

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}

	@Test
	public void shouldAddANewCustomer() {

		// Given
		final Customer newCustomer = Customer.ofType(PERSON).build();

		final ObjectId id = ObjectId.get();
		ReflectionTestUtils.setField(newCustomer, "id", id);

		when(repo.save(any(Customer.class))).thenReturn(newCustomer);

		// When
		final ResponseEntity<?> response = controller.addCustomer(newCustomer);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getHeaders().getLocation().toString()).isEqualTo(format("/customers/%s", id));
	}

	@Test
	public void shouldNotAddACustomerIfCustomerAlreadyExists() throws Exception {

		// Given
		when(repo.existsById(any(ObjectId.class))).thenReturn(true);
		final ObjectId id = ObjectId.get();
		final Customer customer = Customer.ofType(PERSON).build();
		ReflectionTestUtils.setField(customer, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.addCustomer(customer))
			.isInstanceOf(CustomerServiceException.class)
			.hasMessageContaining("Customer already exists");
	}

	@Test
	public void shouldUpdateAnExistingCustomer() {

		// Given
		when(repo.existsById(any(ObjectId.class))).thenReturn(true);
		when(repo.save(any(Customer.class))).thenReturn(Customer.ofType(PERSON).build());
		final ObjectId id = ObjectId.get();
		final Customer existingCustomer = Customer.ofType(CustomerType.PERSON).build();
		ReflectionTestUtils.setField(existingCustomer, "id", id);

		// When
		final ResponseEntity<?> response = controller.updateCustomer(existingCustomer.getId(), existingCustomer);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldFailUpdatingNonExistingCustomer() {

		// Given
		when(repo.existsById(any(ObjectId.class))).thenReturn(false);
		final ObjectId id = ObjectId.get();
		final Customer newCustomer = Customer.ofType(CustomerType.PERSON).build();
		ReflectionTestUtils.setField(newCustomer, "id", id);

		// When
		// Then
		assertThatThrownBy(() -> controller.updateCustomer(newCustomer.getId(), newCustomer))
			.isInstanceOf(CustomerServiceException.class)
			.hasMessageContaining("Customer does not exist");
	}

	@Test
	public void shouldDeleteAnExistingCustomer() {

		// Given
		when(repo.existsById(any(ObjectId.class))).thenReturn(true);
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response = controller.deleteCustomer(id);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	public void shouldDeleteExistingCustomerAndIgnoreSubsequentCalls() throws Exception {

		// Given
		when(repo.existsById(any(ObjectId.class))).thenReturn(true).thenReturn(false);
		final ObjectId id = ObjectId.get();

		// When
		final ResponseEntity<?> response1 = controller.deleteCustomer(id);
		final ResponseEntity<?> response2 = controller.deleteCustomer(id);
		final ResponseEntity<?> response3 = controller.deleteCustomer(id);

		// Then
		assertThat(response1.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response2.getStatusCode()).isEqualTo(NO_CONTENT);
		assertThat(response3.getStatusCode()).isEqualTo(NO_CONTENT);
	}

}
