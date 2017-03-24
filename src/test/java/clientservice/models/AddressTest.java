package clientservice.models;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import clientservice.models.Address;

public class AddressTest {

	@Test
	public void shouldBuildAnAddress() {

		// Given
		final Address address;

		// When
		address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street").city("Shadaloo City")
				.zipcode("123456").build();

		// Then
		assertThat(address).isNotNull();
		assertThat(address.getCountry()).isEqualTo("Shadaloo");
		assertThat(address.getStreetNumber()).isEqualTo(110);
		assertThat(address.getStreetName()).isEqualTo("Bison street");
		assertThat(address.getCity()).isEqualTo("Shadaloo City");
		assertThat(address.getStateOrProvince()).isNull();
		assertThat(address.getZipcode()).isEqualTo("123456");
	}

	@Test
	public void shouldUpdateImmutableAddress() {

		// Given
		Address address = Address.ofCountry("Shadaloo").streetNumber(110).streetName("Bison street")
				.city("Shadaloo City").zipcode("123456").build();

		// When
		address = Address.from(address).streetNumber(2000).build();

		// Then
		assertThat(address).isNotNull();
		assertThat(address.getCountry()).isEqualTo("Shadaloo");
		assertThat(address.getStreetNumber()).isEqualTo(2000);
		assertThat(address.getStreetName()).isEqualTo("Bison street");
		assertThat(address.getCity()).isEqualTo("Shadaloo City");
		assertThat(address.getStateOrProvince()).isNull();
		assertThat(address.getZipcode()).isEqualTo("123456");
	}

	@Test
	public void shouldFailIfCountryIsNull() {

		assertThatThrownBy(() -> Address.ofCountry(null)).hasMessage("Country can not be null.");
	}
}
