package model;

import org.junit.Assert;
import org.junit.Test;

import model.enums.Country;
import model.enums.Province;
import model.enums.State;

public class AddressTest {

	@Test
	public void shouldBuildCanadianAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(Country.CANADA).streetNumber(110).streetName("Rue de la barre").city("Longueuil")
				.province(Province.QUEBEC).postalCode("J4K1A3").build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(Country.CANADA, address.getCountry());
		Assert.assertEquals(110, address.getStreetNumber());
		Assert.assertEquals("Rue de la barre", address.getStreetName());
		Assert.assertEquals("Longueuil", address.getCity());
		Assert.assertEquals(Province.QUEBEC, address.getProvince());
		Assert.assertEquals("J4K1A3", address.getPostalCode());
	}

	@Test
	public void buildUSAAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(Country.USA).address("Wellington street").city("Richmond").state(State.NEW_YORK)
				.zipCode(75023).build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(Country.USA, address.getCountry());
		Assert.assertEquals("Wellington street", address.getAddress());
		Assert.assertEquals("Richmond", address.getCity());
		Assert.assertEquals(State.NEW_YORK, address.getState());
		Assert.assertEquals(75023, address.getZipCode());
	}

}
