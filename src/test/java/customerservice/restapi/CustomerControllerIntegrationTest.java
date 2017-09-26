package customerservice.restapi;

import static customerservice.domain.enums.CustomerType.COMPANY;
import static customerservice.domain.enums.CustomerType.PERSON;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import customerservice.CustomerServiceExceptionHandler;
import customerservice.domain.Customer;
import customerservice.repository.mongodb.CustomerRepository;
import customerservice.restapi.CustomerController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { CustomerController.class })
@ContextConfiguration(classes = { CustomerServiceExceptionHandler.class })
@ComponentScan
public class CustomerControllerIntegrationTest {

	/**
	 * Disable Spring Security OAuth2 authentication.
	 * 
	 */
	//@Order(SecurityProperties.IGNORED_ORDER)
	@Configuration
	static class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().anyRequest();
		}
	}

	@MockBean
	private CustomerRepository repo;

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldReturnAllCustomers() throws Exception {

		final List<Customer> customers = asList(Customer.ofType(PERSON).build(), Customer.ofType(COMPANY).build());

		given(repo.findAll()).willReturn(customers);

		// Expect HTTP 200
		mvc.perform(get("/customers").accept(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(jsonPath("$..customer_type").isArray())
				.andExpect(jsonPath("$..customer_type").value(hasItems(PERSON.toString(), COMPANY.toString())));
	}

	@Test
	public void shouldReturnOneCustomerById() throws Exception {

		final LocalDate birthDate = LocalDate.of(1990, Month.JULY, 31);
		final Customer customer = Customer.ofType(PERSON).withBirthDate(birthDate).build();
		final ObjectId id = ObjectId.get();

		given(repo.findById(any(ObjectId.class))).willReturn(Optional.of(customer));

		// Expect HTTP 200
		mvc.perform(get(String.format("/customers/%s", id)).accept(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$..customer_type").value("PERSON"))
				.andExpect(jsonPath("$..birth_date").value("1990-07-31"));
	}

	@Test
	public void shouldReturn404IfCustomerNotFound() throws Exception {

		given(repo.findById(any(ObjectId.class))).willReturn(Optional.empty());

		// Expect HTTP 404
		mvc.perform(get(String.format("/customers/%s", ObjectId.get())).accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound()).andExpect(content().string(""));
	}

	@Test
	public void shouldReturnEmptyBodyWhenNoCustomers() throws Exception {

		given(repo.findAll()).willReturn(Collections.emptyList());

		// Expect HTTP 204
		mvc.perform(get("/customers").accept(APPLICATION_JSON_UTF8)).andExpect(status().isNoContent());
	}

	@Test
	public void shouldReturnHeadersOnly() throws Exception {

		final List<Customer> customers = asList(Customer.ofType(PERSON).build(), Customer.ofType(COMPANY).build());

		given(repo.findAll()).willReturn(customers);

		// Expect HTTP 200
		mvc.perform(head("/customers").accept(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}

	@Test
	public void shouldAddANewCustomer() throws Exception {

		final Customer newCustomer = Customer.ofType(PERSON).build();

		final ObjectId id = ObjectId.get();
		ReflectionTestUtils.setField(newCustomer, "id", id);

		given(repo.save(any(Customer.class))).willReturn(newCustomer);

		// Expect HTTP 201
		mvc.perform(post("/customers").contentType(APPLICATION_JSON_UTF8).content("{\"customer_type\":\"PERSON\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", is(equalTo(String.format("/customers/%s", id)))));
	}

	@Test
	public void shouldNotAddCustomerIfContentIsNotValid() throws Exception {

		final String BAD_JSON = "{\"customer_type_is_missing\":\"PERSON\"}";

		// Expect HTTP 400
		mvc.perform(post("/customers").contentType(APPLICATION_JSON_UTF8).content(BAD_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldNotAddCustomerIfCustomerAlreadyExists() throws Exception {

		given(repo.existsById(any(ObjectId.class))).willReturn(true);
		final ObjectId id = ObjectId.get();

		// Expect HTTP 400
		final String EXISTING_CLIENT = String.format("{\"id\":\"%s\",\"customer_type\":\"COMPANY\"}", id);
		mvc.perform(post("/customers").contentType(APPLICATION_JSON_UTF8).content(EXISTING_CLIENT))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldUpdateAnExistingCustomer() throws Exception {

		given(repo.existsById(any(ObjectId.class))).willReturn(true);
		given(repo.save(any(Customer.class))).willReturn(Customer.ofType(PERSON).build());

		final ObjectId id = ObjectId.get();
		final String UPDATE = String.format(
				"{\"id\":\"%s\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"customer_type\":\"COMPANY\"}", id);

		// Expect HTTP 204
		mvc.perform(put(String.format("/customers/%s", id)).contentType(APPLICATION_JSON_UTF8).content(UPDATE))
				.andExpect(status().isNoContent());
	}

	@Test
	public void shouldFailUpdatingNonExistingCustomer() throws Exception {

		given(repo.existsById(any(ObjectId.class))).willReturn(false);

		final ObjectId id = ObjectId.get();
		final String UPDATE = String.format(
				"{\"id\":\"%s\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"customer_type\":\"COMPANY\"}", id);

		// Expect HTTP 204
		mvc.perform(put(String.format("/customers/%s", id)).contentType(APPLICATION_JSON_UTF8).content(UPDATE))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldDeleteAnExistingCustomer() throws Exception {

		given(repo.existsById(any(ObjectId.class))).willReturn(true);

		final ObjectId id = ObjectId.get();

		// Expect HTTP 204
		mvc.perform(delete(String.format("/customers/%s", id))).andExpect(status().isNoContent());
	}

	@Test
	public void shouldDeleteExistingCustomerAndIgnoreFollowingCalls() throws Exception {

		given(repo.existsById(any(ObjectId.class))).willReturn(true).willReturn(false);

		final ObjectId id = ObjectId.get();

		// Expect HTTP 204 for each call
		mvc.perform(delete(String.format("/customers/%s", id))).andExpect(status().isNoContent());
		mvc.perform(delete(String.format("/customers/%s", id))).andExpect(status().isNoContent());
		mvc.perform(delete(String.format("/customers/%s", id))).andExpect(status().isNoContent());
	}
}
