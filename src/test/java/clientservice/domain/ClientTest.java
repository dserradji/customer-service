package clientservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import clientservice.domain.Address;
import clientservice.domain.Client;
import clientservice.domain.enums.ClientType;
import clientservice.domain.enums.Gender;
import clientservice.domain.enums.MaritalStatus;
import clientservice.domain.enums.PhoneType;

public class ClientTest {

	@Test
	public void shouldBuildAClientOfTypePerson() {

		// Given
		final Client client;
		final Address address = Address.ofCountry("Shadaloo")
				.withStreetNumber(110)
				.withStreetName("Bison street")
				.withCity("Shadaloo City")
				.withZipcode("123456").build();

		// When
		client = Client.ofType(ClientType.PERSON)
				.withFirstName("Ken")
				.withLastName("Masters")
				.withGender(Gender.MALE)
				.withBirthDate(LocalDate.of(1990, Month.MARCH, 16))
				.withMaritalStatus(MaritalStatus.SINGLE)
				.withAddress(address)
				.withPhone(PhoneType.HOME, "111111111")
				.withPhone(PhoneType.CELLULAR, "222222222")
				.withPhone(PhoneType.OFFICE, "333333333 Ext123")
				.withPhone(PhoneType.FAX, "444444444")
				.withEmail("kmasters@streetf.com")
				.build();

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
		final Address address = Address.ofCountry("Shadaloo")
				.withStreetNumber(110)
				.withStreetName("Bison street")
				.withCity("Shadaloo City")
				.withZipcode("123456")
				.build();

		// When
		client = Client.ofType(ClientType.COMPANY)
				.withLastName("Acme Corp.")
				.withAddress(address)
				.withPhone(PhoneType.HOME, "111111111")
				.withPhone(PhoneType.CELLULAR, "222222222")
				.withPhone(PhoneType.OFFICE, "333333333 Ext123")
				.withPhone(PhoneType.FAX, "444444444")
				.withEmail("kmasters@streetf.com")
				.build();

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
	public void shouldUpdateAClient() {

		// Given
		final Address address = Address.ofCountry("Shadaloo").withStreetNumber(110).withStreetName("Bison street")
				.withCity("Shadaloo City").withZipcode("123456").build();

		Client client = Client.ofType(ClientType.COMPANY)
				.withLastName("Acme Corp.")
				.withAddress(address)
				.withPhone(PhoneType.HOME, "111111111")
				.withPhone(PhoneType.CELLULAR, "222222222")
				.withPhone(PhoneType.OFFICE, "333333333 Ext123")
				.withPhone(PhoneType.FAX, "444444444")
				.withEmail("kmasters@streetf.com")
				.build();

		// When
		client = Client.from(client).withLastName("Acme Inc.").build();

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
