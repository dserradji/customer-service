package model;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

import model.enums.AddressType;
import model.enums.Gender;
import model.enums.Language;
import model.enums.MaritalStatus;
import model.enums.NameSuffix;
import model.enums.PhoneType;
import model.enums.Province;

public class ClientTest {

	@Test
	public void shouldCreateAndInitializeAClient() {

		// Given
		final Client client = new Client();
		final Address address = Address.of(AddressType.CANADA).streetNumber(110).streetName("Rue de la barre")
				.city("Longueuil").province(Province.QUEBEC).postalCode("J4K1A3").build();

		// When
		client.setSuffix(NameSuffix.SENIOR);
		client.setFirstName("John");
		client.setLastName("Doe");
		client.setGender(Gender.MALE);
		client.setBirthDate(LocalDate.of(1990, Month.MARCH, 16));
		client.setMaritalStatus(MaritalStatus.SINGLE);
		client.setEstate(false);
		client.setLanguage(Language.FRENCH);
		client.setSolicited(false);
		client.setIntactEmployee(false);
		client.setAddress(address);
		client.addPhone(PhoneType.HOME, "514.553.3110");
		client.addPhone(PhoneType.CELLULAR, "+1.514.553.3110");
		client.addPhone(PhoneType.OFFICE, "5145533110 Ext123");
		client.addPhone(PhoneType.FAX, "+15145533110");
		client.setEmail("email@isp.com");
		client.setInsuranceCompany("Intact");
		client.setInsuranceCompanyProvince(Province.QUEBEC);

		// Then
		Assert.assertNotNull(client);
		Assert.assertEquals(NameSuffix.SENIOR, client.getSuffix());
		Assert.assertEquals("John", client.getFirstName());
		Assert.assertEquals("Doe", client.getLastName());
		Assert.assertEquals(Gender.MALE, client.getGender());
		Assert.assertEquals(LocalDate.of(1990, Month.MARCH, 16), client.getBirthDate());
		Assert.assertEquals(MaritalStatus.SINGLE, client.getMaritalStatus());
		Assert.assertEquals(false, client.isEstate());
		Assert.assertEquals(Language.FRENCH, client.getLanguage());
		Assert.assertEquals(false, client.isSolicited());
		Assert.assertEquals(false, client.isIntactEmployee());
		Assert.assertEquals(address, client.getAddress());
		Assert.assertEquals("514.553.3110", client.getPhoneNumber(PhoneType.HOME));
		Assert.assertEquals("+1.514.553.3110", client.getPhoneNumber(PhoneType.CELLULAR));
		Assert.assertEquals("5145533110 Ext123", client.getPhoneNumber(PhoneType.OFFICE));
		Assert.assertEquals("+15145533110", client.getPhoneNumber(PhoneType.FAX));
		Assert.assertEquals("email@isp.com", client.getEmail());
		Assert.assertEquals("Intact", client.getInsuranceCompany());
		Assert.assertEquals(Province.QUEBEC, client.getInsuranceCompanyProvince());
	}
}
