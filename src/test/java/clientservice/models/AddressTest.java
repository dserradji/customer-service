package clientservice.models;

import org.junit.Assert;
import org.junit.Test;

import clientservice.models.Address;
import clientservice.models.enums.AddressType;
import clientservice.models.enums.Province;
import clientservice.models.enums.State;

public class AddressTest {

	@Test
	public void shouldBuildCanadianAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofCanada().streetNumber(110).streetName("Rue de la barre").city("Longueuil")
				.province(Province.QUEBEC).postalCode("J4K1A3").build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.CANADA, address.getType());
		Assert.assertEquals(110, address.getStreetNumber());
		Assert.assertEquals("Rue de la barre", address.getStreetName());
		Assert.assertEquals("Longueuil", address.getCity());
		Assert.assertEquals(Province.QUEBEC, address.getProvince());
		Assert.assertEquals("J4K1A3", address.getPostalCode());
	}

	@Test
	public void shouldBuildEmptyCanadianAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofCanada().build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.CANADA, address.getType());
		Assert.assertEquals(0, address.getStreetNumber());
		Assert.assertNull(address.getStreetName());
		Assert.assertNull(address.getCity());
		Assert.assertNull(address.getProvince());
		Assert.assertNull(address.getPostalCode());
	}

	@Test
	public void shouldBuildUSAAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofUSA().addressLine("Wellington street").city("Richmond").state(State.NEW_YORK).zipCode(75023)
				.build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.USA, address.getType());
		Assert.assertEquals("Wellington street", address.getAddressLine());
		Assert.assertEquals("Richmond", address.getCity());
		Assert.assertEquals(State.NEW_YORK, address.getState());
		Assert.assertEquals(75023, address.getZipCode());
	}

	@Test
	public void shouldBuildEmptyUSAAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofUSA().build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.USA, address.getType());
		Assert.assertNull(address.getAddressLine());
		Assert.assertNull(address.getCity());
		Assert.assertNull(address.getState());
		Assert.assertEquals(0, address.getZipCode());
	}

	@Test
	public void shouldBuildInternationalAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofOthers().withLine("71 rue Chaptal").withLine("Levallois-Perret").withLine("92300")
				.country("France").build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.OTHERS, address.getType());
		Assert.assertEquals("71 rue Chaptal", address.getLines().get(0));
		Assert.assertEquals("Levallois-Perret", address.getLines().get(1));
		Assert.assertEquals("92300", address.getLines().get(2));
		Assert.assertEquals("France", address.getCountry());
	}

	@Test
	public void shouldBuildEmptyInternationalAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofOthers().build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.OTHERS, address.getType());
		Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(address.getLines().size()));
		Assert.assertNull(address.getCountry());
	}
}
