package model;

import org.junit.Assert;
import org.junit.Test;

import model.enums.AddressType;
import model.enums.Province;
import model.enums.State;

public class AddressTest {

	@Test
	public void shouldBuildCanadianAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(AddressType.CANADA).streetNumber(110).streetName("Rue de la barre").city("Longueuil")
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
		address = Address.of(AddressType.CANADA).build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.CANADA, address.getType());
		Assert.assertEquals(Integer.MIN_VALUE, address.getStreetNumber());
		Assert.assertNull(address.getStreetName());
		Assert.assertNull(address.getCity());
		Assert.assertNull(address.getProvince());
		Assert.assertNull(address.getPostalCode());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldFailWhenAddingAddressLineToCanadianAddress() {
		Address.of(AddressType.CANADA).address("Wellington str.").build();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldFailWhenAddingZipCodeToCanadianAddress() {
		Address.of(AddressType.CANADA).zipCode(75000).build();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldFailWhenAddingStateToCanadianAddress() {
		Address.of(AddressType.CANADA).state(State.NEW_YORK).build();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldFailWhenBuildingCanadianAddressAsLines() {
		Address.of(AddressType.CANADA).withLine("110 rue de la barre").withLine("Longueuil, QC").build();
	}

	@Test
	public void shouldBuildUSAAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(AddressType.USA).address("Wellington street").city("Richmond").state(State.NEW_YORK)
				.zipCode(75023).build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.USA, address.getType());
		Assert.assertEquals("Wellington street", address.getAddress());
		Assert.assertEquals("Richmond", address.getCity());
		Assert.assertEquals(State.NEW_YORK, address.getState());
		Assert.assertEquals(75023, address.getZipCode());
	}

	@Test
	public void shouldBuildEmptyUSAAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(AddressType.USA).build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.USA, address.getType());
		Assert.assertNull(address.getAddress());
		Assert.assertNull(address.getCity());
		Assert.assertNull(address.getState());
		Assert.assertEquals(Integer.MIN_VALUE, address.getZipCode());
	}

	@Test
	public void shouldBuildInternationalAddress() {

		// Given
		final Address address;

		// When
		address = Address.of(AddressType.OTHERS).withLine("71 rue Chaptal").withLine("Levallois-Perret")
				.withLine("92300").country("France").build();

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
		address = Address.of(AddressType.OTHERS).build();

		// Then
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressType.OTHERS, address.getType());
		Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(address.getLines().size()));
		Assert.assertNull(address.getCountry());
	}
}
