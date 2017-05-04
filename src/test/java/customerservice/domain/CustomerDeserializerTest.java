package customerservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import customerservice.CustomerServiceException;
import customerservice.domain.Customer;
import customerservice.domain.enums.CustomerType;
import customerservice.domain.enums.Gender;
import customerservice.domain.enums.MaritalStatus;
import customerservice.domain.enums.PhoneType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CustomerDeserializerTest {

	@Autowired
	private ObjectMapper mapper;

	final String id = "{\"id\":\"58e94dffebbd721e30c97d3d\",\"customer_type\":\"PERSON\"}";

	@Test
	public void shouldDeserializeIdField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"id\":\"58e94dffebbd721e30c97d3d\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getId()).isEqualTo(new ObjectId("58e94dffebbd721e30c97d3d"));
	}

	@Test
	public void shouldDeserializeFirstNameField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"first_name\":\"John\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getFirstName()).isEqualTo("John");
	}

	@Test
	public void shouldDeserializeLastNameField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"last_name\":\"Doe\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getLastName()).isEqualTo("Doe");
	}

	@Test
	public void shouldDeserializeGenderField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"gender\":\"MALE\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getGender()).isEqualTo(Gender.MALE);
	}

	@Test
	public void shouldDeserializeBirthDateField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"birth_date\":\"1990-01-20\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getBirthDate()).isEqualTo(LocalDate.of(1990, Month.JANUARY, 20));
	}

	@Test
	public void shouldDeserializeMaritalStatusField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"marital_status\":\"SINGLE\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
	}

	@Test
	public void shouldDeserializeAddressField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"address\":{\"street_number\":\"100\",\"street_name\":\"Wellington\",\"city\":\"Shadaloo City\",\"zipcode\":\"12345\",\"state_or_province\":\"stateOrProvince\",\"country\":\"Shadaloo\"},\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getAddress()).isNotNull();
		assertThat(customer.getAddress().getStreetNumber()).isEqualTo(100);
		assertThat(customer.getAddress().getStreetName()).isEqualTo("Wellington");
		assertThat(customer.getAddress().getCity()).isEqualTo("Shadaloo City");
		assertThat(customer.getAddress().getZipcode()).isEqualTo("12345");
		assertThat(customer.getAddress().getStateOrProvince()).isEqualTo("stateOrProvince");
		assertThat(customer.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldDeserializePhonesField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"phones\":{\"HOME\":\"11111\",\"CELLULAR\":\"22222\",\"OFFICE\":\"33333\",\"FAX\":\"44444\"},\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getPhones()).isNotNull();
		assertThat(customer.getPhones().size()).isEqualTo(4);
		assertThat(customer.getPhones().get(PhoneType.HOME)).isEqualTo("11111");
		assertThat(customer.getPhones().get(PhoneType.CELLULAR)).isEqualTo("22222");
		assertThat(customer.getPhones().get(PhoneType.OFFICE)).isEqualTo("33333");
		assertThat(customer.getPhones().get(PhoneType.FAX)).isEqualTo("44444");
	}

	@Test
	public void shouldDeserializeEmailField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"email\":\"jdoe@email.com\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
		assertThat(customer.getEmail()).isEqualTo("jdoe@email.com");
	}

	@Test
	public void shouldFailWhenClienTypeIsMissing() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"id\":\"58e94dffebbd721e30c97d3d\"}";

		// When
		assertThatThrownBy(() -> mapper.readValue(json, Customer.class)).isInstanceOf(CustomerServiceException.class)
				.hasMessageContaining("Customer type can not be null");
	}

	@Test
	public void shouldFailWhenAddressIsMissingcountry() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"address\":{\"street_number\":\"100\",\"street_name\":\"Wellington\",\"city\":\"Shadaloo City\",\"zipcode\":\"12345\",\"state_or_province\":\"stateOrProvince\"},\"customer_type\":\"PERSON\"}";

		// When
		assertThatThrownBy(() -> mapper.readValue(json, Customer.class)).isInstanceOf(CustomerServiceException.class)
				.hasMessageContaining("Country can not be null");
	}

	@Test
	public void shouldIgnoreUnknownFieldsAndMapCustomerType()
			throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"unknown1\":\"aaaaaa\",\"unknown2\":\"bbbbbb\",\"unknown3\":\"cccccc\",\"customer_type\":\"PERSON\"}";

		// When
		final Customer customer = mapper.readValue(json, Customer.class);

		// Then
		assertThat(customer).isNotNull();
		assertThat(customer.getCustomerType()).isEqualTo(CustomerType.PERSON);
	}

	@Test
	public void shouldIgnoreUnknownFieldsAndFailBecauseNoCustomerTypeIsProvided()
			throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"unknown1\":\"aaaaaa\",\"unknown2\":\"bbbbbb\",\"unknown3\":\"cccccc\",\"first_name\":\"John\"}";

		// When
		assertThatThrownBy(() -> mapper.readValue(json, Customer.class)).isInstanceOf(CustomerServiceException.class)
				.hasMessageContaining("Customer type can not be null.");
	}
}
