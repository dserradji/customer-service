package model;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

import model.enums.ClientType;
import model.enums.Gender;
import model.enums.Language;
import model.enums.MaritalStatus;
import model.enums.NameSuffix;
import model.enums.PhoneType;
import model.enums.Province;

public class ClientTest {

	@Test
	public void shouldBuildAClientOfTypePerson() {

		// Given
		final Client client;
		final Address address = Address.ofCanada().streetNumber(110).streetName("Rue de la barre").city("Longueuil")
				.province(Province.QUEBEC).postalCode("J4K1A3").build();

		// When
		client = Client.ofType(ClientType.PERSON).withFirstName("John").withLastName("Doe")
				.withSuffix(NameSuffix.SENIOR).withGender(Gender.MALE)
				.withBirthDate(LocalDate.of(1990, Month.MARCH, 16)).withMaritalStatus(MaritalStatus.SINGLE)
				.isEstate(false).withLanguage(Language.FRENCH).isSolicited(false).isIntactEmployee(false)
				.withAddress(address).withPhone(PhoneType.HOME, "514.553.3110")
				.withPhone(PhoneType.CELLULAR, "+1.514.553.3110").withPhone(PhoneType.OFFICE, "5145533110 Ext123")
				.withPhone(PhoneType.FAX, "+15145533110").withEmail("johnd@email.com")
				.withInsuranceCompany(InsuranceCompany.INTACT).withInsuranceCompanyProvince(Province.ONTARIO).build();

		// Then
		Assert.assertNotNull(client);
		Assert.assertEquals(ClientType.PERSON, client.getClientType());
		Assert.assertEquals(NameSuffix.SENIOR, client.getSuffix());
		Assert.assertEquals("John", client.getFirstName());
		Assert.assertEquals("Doe", client.getLastName());
		Assert.assertEquals(Gender.MALE, client.getGender());
		Assert.assertEquals(LocalDate.of(1990, Month.MARCH, 16), client.getBirthDate());
		Assert.assertEquals(MaritalStatus.SINGLE, client.getMaritalStatus());
		Assert.assertFalse(client.isEstate());
		Assert.assertEquals(Language.FRENCH, client.getLanguage());
		Assert.assertFalse(client.isSolicited());
		Assert.assertFalse(client.isIntactEmployee());
		Assert.assertEquals(address, client.getAddress());
		Assert.assertEquals("514.553.3110", client.getPhoneNumber(PhoneType.HOME));
		Assert.assertEquals("+1.514.553.3110", client.getPhoneNumber(PhoneType.CELLULAR));
		Assert.assertEquals("5145533110 Ext123", client.getPhoneNumber(PhoneType.OFFICE));
		Assert.assertEquals("+15145533110", client.getPhoneNumber(PhoneType.FAX));
		Assert.assertEquals("johnd@email.com", client.getEmail());
		Assert.assertEquals(InsuranceCompany.INTACT, client.getInsuranceCompany());
		Assert.assertEquals(Province.ONTARIO, client.getInsuranceCompanyProvince());
	}

	@Test
	public void shouldBuildAClientOfTypeCompany() {

		// Given
		final Client client;
		final Address address = Address.ofCanada().streetNumber(110).streetName("Rue de la barre").city("Longueuil")
				.province(Province.QUEBEC).postalCode("J4K1A3").build();

		// When
		client = Client.ofType(ClientType.COMPANY).withLastName("ACME Inc.").withLanguage(Language.FRENCH)
				.isSolicited(false).isIntactEmployee(false).withAddress(address)
				.withPhone(PhoneType.HOME, "514.553.3110").withPhone(PhoneType.CELLULAR, "+1.514.553.3110")
				.withPhone(PhoneType.OFFICE, "5145533110 Ext123").withPhone(PhoneType.FAX, "+15145533110")
				.withEmail("johnd@email.com").withInsuranceCompany(InsuranceCompany.INTACT)
				.withInsuranceCompanyProvince(Province.ONTARIO).build();

		// Then
		Assert.assertNotNull(client);
		Assert.assertEquals(ClientType.COMPANY, client.getClientType());
		Assert.assertNull(client.getSuffix());
		Assert.assertNull(client.getFirstName());
		Assert.assertEquals("ACME Inc.", client.getLastName());
		Assert.assertNull(client.getGender());
		Assert.assertNull(client.getBirthDate());
		Assert.assertNull(client.getMaritalStatus());
		Assert.assertFalse(client.isEstate());
		Assert.assertEquals(Language.FRENCH, client.getLanguage());
		Assert.assertFalse(client.isSolicited());
		Assert.assertFalse(client.isIntactEmployee());
		Assert.assertEquals(address, client.getAddress());
		Assert.assertEquals("514.553.3110", client.getPhoneNumber(PhoneType.HOME));
		Assert.assertEquals("+1.514.553.3110", client.getPhoneNumber(PhoneType.CELLULAR));
		Assert.assertEquals("5145533110 Ext123", client.getPhoneNumber(PhoneType.OFFICE));
		Assert.assertEquals("+15145533110", client.getPhoneNumber(PhoneType.FAX));
		Assert.assertEquals("johnd@email.com", client.getEmail());
		Assert.assertEquals(InsuranceCompany.INTACT, client.getInsuranceCompany());
		Assert.assertEquals(Province.ONTARIO, client.getInsuranceCompanyProvince());
	}
}
