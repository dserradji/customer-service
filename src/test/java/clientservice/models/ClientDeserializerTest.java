package clientservice.models;

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

import clientservice.models.enums.ClientType;
import clientservice.models.enums.Gender;
import clientservice.models.enums.MaritalStatus;
import clientservice.models.enums.PhoneType;
import clientservice.restapi.ClientResourceException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ClientDeserializerTest {

	@Autowired
	private ObjectMapper mapper;

	final String id = "{\"id\":\"58e94dffebbd721e30c97d3d\",\"client_type\":\"PERSON\"}";

	@Test
	public void shouldDeserializeIdField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"id\":\"58e94dffebbd721e30c97d3d\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getId()).isEqualTo(new ObjectId("58e94dffebbd721e30c97d3d"));
	}

	@Test
	public void shouldDeserializeFirstNameField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"first_name\":\"John\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getFirstName()).isEqualTo("John");
	}

	@Test
	public void shouldDeserializeLastNameField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"last_name\":\"Doe\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getLastName()).isEqualTo("Doe");
	}

	@Test
	public void shouldDeserializeGenderField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"gender\":\"MALE\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getGender()).isEqualTo(Gender.MALE);
	}

	@Test
	public void shouldDeserializeBirthDateField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"birth_date\":\"1990-01-20\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getBirthDate()).isEqualTo(LocalDate.of(1990, Month.JANUARY, 20));
	}

	@Test
	public void shouldDeserializeMaritalStatusField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"marital_status\":\"SINGLE\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
	}

	@Test
	public void shouldDeserializeAddressField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"address\":{\"street_number\":\"100\",\"street_name\":\"Wellington\",\"city\":\"Shadaloo City\",\"zipcode\":\"12345\",\"state_or_province\":\"stateOrProvince\",\"country\":\"Shadaloo\"},\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getAddress()).isNotNull();
		assertThat(client.getAddress().getStreetNumber()).isEqualTo(100);
		assertThat(client.getAddress().getStreetName()).isEqualTo("Wellington");
		assertThat(client.getAddress().getCity()).isEqualTo("Shadaloo City");
		assertThat(client.getAddress().getZipcode()).isEqualTo("12345");
		assertThat(client.getAddress().getStateOrProvince()).isEqualTo("stateOrProvince");
		assertThat(client.getAddress().getCountry()).isEqualTo("Shadaloo");
	}

	@Test
	public void shouldDeserializePhonesField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"phones\":{\"HOME\":\"11111\",\"CELLULAR\":\"22222\",\"OFFICE\":\"33333\",\"FAX\":\"44444\"},\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getPhones()).isNotNull();
		assertThat(client.getPhones().size()).isEqualTo(4);
		assertThat(client.getPhones().get(PhoneType.HOME)).isEqualTo("11111");
		assertThat(client.getPhones().get(PhoneType.CELLULAR)).isEqualTo("22222");
		assertThat(client.getPhones().get(PhoneType.OFFICE)).isEqualTo("33333");
		assertThat(client.getPhones().get(PhoneType.FAX)).isEqualTo("44444");
	}

	@Test
	public void shouldDeserializeEmailField() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"email\":\"jdoe@email.com\",\"client_type\":\"PERSON\"}";

		// When
		final Client client = mapper.readValue(json, Client.class);

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getEmail()).isEqualTo("jdoe@email.com");
	}

	@Test
	public void shouldFailWhenClienTypeIsMissing() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"id\":\"58e94dffebbd721e30c97d3d\"}";

		// When
		assertThatThrownBy(() -> mapper.readValue(json, Client.class)).isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Client type can not be null");
	}

	@Test
	public void shouldFailWhenAddressIsMissingcountry() throws JsonParseException, JsonMappingException, IOException {

		// Given
		final String json = "{\"address\":{\"street_number\":\"100\",\"street_name\":\"Wellington\",\"city\":\"Shadaloo City\",\"zipcode\":\"12345\",\"state_or_province\":\"stateOrProvince\"},\"client_type\":\"PERSON\"}";

		// When
		assertThatThrownBy(() -> mapper.readValue(json, Client.class)).isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Country can not be null");
	}
}
