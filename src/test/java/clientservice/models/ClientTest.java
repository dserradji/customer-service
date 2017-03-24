package clientservice.models;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import clientservice.models.enums.ClientType;
import clientservice.models.enums.Gender;
import clientservice.models.enums.MaritalStatus;
import clientservice.models.enums.PhoneType;

public class ClientTest {

	@Test
	public void shouldBuildAClientOfTypePerson() {

		// Given
		final Client client;
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		// When
		client = Client.ofType(ClientType.PERSON).firstName("Ken").lastName("Masters").gender(Gender.MALE)
				.birthDate(LocalDate.of(1990, Month.MARCH, 16)).maritalStatus(MaritalStatus.SINGLE).address(address)
				.phone(PhoneType.HOME, "111111111").phone(PhoneType.CELLULAR, "222222222")
				.phone(PhoneType.OFFICE, "333333333 Ext123").phone(PhoneType.FAX, "444444444")
				.email("kmasters@streetf.com").build();

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.PERSON);
		assertThat(client.getFirstName()).isEqualTo("Ken");
		assertThat(client.getLastName()).isEqualTo("Masters");
		assertThat(client.getGender()).isEqualTo(Gender.MALE);
		assertThat(client.getBirthDate()).isEqualTo(LocalDate.of(1990, Month.MARCH, 16));
		assertThat(client.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
		assertThat(client.getAddress().getZipcode()).isEqualTo("123456");
		assertThat(client.getPhones()).contains(
				entry(PhoneType.HOME, "111111111"),
				entry(PhoneType.CELLULAR, "222222222"), 
				entry(PhoneType.OFFICE, "333333333 Ext123"),
				entry(PhoneType.FAX, "444444444"));
		assertThat(client.getEmail()).isEqualTo("kmasters@streetf.com");
	}

	@Test
	public void shouldBuildAClientOfTypeCompany() {

		// Given
		final Client client;
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		// When
		client = Client.ofType(ClientType.COMPANY).lastName("Acme Corp.").address(address)
				.phone(PhoneType.HOME, "111111111").phone(PhoneType.CELLULAR, "222222222")
				.phone(PhoneType.OFFICE, "333333333 Ext123").phone(PhoneType.FAX, "444444444")
				.email("kmasters@streetf.com").build();

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.COMPANY);
		assertThat(client.getLastName()).isEqualTo("Acme Corp.");
		assertThat(client.getGender()).isNull();
		assertThat(client.getBirthDate()).isNull();
		assertThat(client.getMaritalStatus()).isNull();
		assertThat(client.getAddress().getZipcode()).isEqualTo("123456");
		assertThat(client.getPhones()).contains(
				entry(PhoneType.HOME, "111111111"),
				entry(PhoneType.CELLULAR, "222222222"), 
				entry(PhoneType.OFFICE, "333333333 Ext123"),
				entry(PhoneType.FAX, "444444444"));
		assertThat(client.getEmail()).isEqualTo("kmasters@streetf.com");
	}

	@Test
	public void shouldUpdateTheClient() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		Client client = Client.ofType(ClientType.COMPANY).lastName("Acme Corp.").address(address)
				.phone(PhoneType.HOME, "111111111").phone(PhoneType.CELLULAR, "222222222")
				.phone(PhoneType.OFFICE, "333333333 Ext123").phone(PhoneType.FAX, "444444444")
				.email("kmasters@streetf.com").build();

		// When
		client = Client.from(client).lastName("Acme Inc.").build();

		// Then
		assertThat(client).isNotNull();
		assertThat(client.getClientType()).isEqualTo(ClientType.COMPANY);
		assertThat(client.getLastName()).isEqualTo("Acme Inc.");
		assertThat(client.getGender()).isNull();
		assertThat(client.getBirthDate()).isNull();
		assertThat(client.getMaritalStatus()).isNull();
		assertThat(client.getAddress().getZipcode()).isEqualTo("123456");
		assertThat(client.getPhones()).contains(
				entry(PhoneType.HOME, "111111111"),
				entry(PhoneType.CELLULAR, "222222222"), 
				entry(PhoneType.OFFICE, "333333333 Ext123"),
				entry(PhoneType.FAX, "444444444"));
		assertThat(client.getEmail()).isEqualTo("kmasters@streetf.com");
	}

	@Test
	public void shouldFailIfClientTypeIsNull() {
		assertThatThrownBy(() -> Client.ofType(null).build()).hasMessage("Client type can not be null.");
	}
}
