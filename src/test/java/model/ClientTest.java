package model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import model.enums.ClientType;
import model.enums.Gender;
import model.enums.MaritalStatus;
import model.enums.PhoneType;

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
		assertThat(client, is(notNullValue()));
		assertThat(client.getClientType(), is(equalTo(ClientType.PERSON)));
		assertThat(client.getFirstName(), is(equalTo("Ken")));
		assertThat(client.getLastName(), is(equalTo("Masters")));
		assertThat(client.getGender(), is(equalTo(Gender.MALE)));
		assertThat(client.getBirthDate(), is(equalTo(LocalDate.of(1990, Month.MARCH, 16))));
		assertThat(client.getMaritalStatus(), is(equalTo(MaritalStatus.SINGLE)));
		assertThat(client.getAddress().getZipcode(), is(equalTo("123456")));
		assertThat(client.getPhones().get(PhoneType.HOME), is(equalTo("111111111")));
		assertThat(client.getPhones().get(PhoneType.CELLULAR), is(equalTo("222222222")));
		assertThat(client.getPhones().get(PhoneType.OFFICE), is(equalTo("333333333 Ext123")));
		assertThat(client.getPhones().get(PhoneType.FAX), is(equalTo("444444444")));
		assertThat(client.getEmail(), is(equalTo("kmasters@streetf.com")));
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
		assertThat(client, is(notNullValue()));
		assertThat(client.getClientType(), is(equalTo(ClientType.COMPANY)));
		assertThat(client.getLastName(), is(equalTo("Acme Corp.")));
		assertThat(client.getGender(), is(nullValue()));
		assertThat(client.getBirthDate(), is(nullValue()));
		assertThat(client.getMaritalStatus(), is(nullValue()));
		assertThat(client.getAddress().getZipcode(), is(equalTo("123456")));
		assertThat(client.getPhones().get(PhoneType.HOME), is(equalTo("111111111")));
		assertThat(client.getPhones().get(PhoneType.CELLULAR), is(equalTo("222222222")));
		assertThat(client.getPhones().get(PhoneType.OFFICE), is(equalTo("333333333 Ext123")));
		assertThat(client.getPhones().get(PhoneType.FAX), is(equalTo("444444444")));
		assertThat(client.getEmail(), is(equalTo("kmasters@streetf.com")));
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailIfClientTypeIsNull() {
		Client.ofType(null).build();
	}
}
