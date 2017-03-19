package model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AddressTest {

	@Test
	public void shouldBuildAnAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street").city("Shadaloo City")
				.zipcode("123456").build();

		// Then
		assertThat(address, is(notNullValue()));
		assertThat(address.getCountry(), is(equalTo("Shadaloo")));
		assertThat(address.getStreetNumber(), is(equalTo(110)));
		assertThat(address.getStreetName(), is(equalTo("Bison street")));
		assertThat(address.getCity(), is(equalTo("Shadaloo City")));
		assertThat(address.getStateOrProvince(), is(nullValue()));
		assertThat(address.getZipcode(), is(equalTo("123456")));
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailIfCountryIsNull() {

		Address.ofCountry(null).build();
	}
}
