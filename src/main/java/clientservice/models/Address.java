package clientservice.models;

import java.util.Objects;

/**
 * {@code Address} is an immutable object.
 * <p>
 * Use {@code Address.ofCountry(String)} to create a new address.
 * <p>
 * In the absence of setters use {@code Address.from(Address)} to update one or
 * more fields of a given address, a copy of that address containing the updated
 * fields is returned.<br>
 * <p>
 * Example:<br>
 * {@code Address address = Address.ofCountry("Canada").streetNumber(100).build();}<br>
 * {@code address = Address.from(address).streetNumber(200).build();}
 *
 */
public final class Address {

	private int streetNumber;
	private String streetName;
	private String city;
	private String zipcode;
	private String stateOrProvince;
	private String country;

	private Address() {
		// Needed by Spring Data
	}

	private Address(int streetNumber, String streetName, String city, String zipcode, String stateOrProvince,
			String country) {
		this.streetNumber = streetNumber;
		this.streetName = streetName;
		this.city = city;
		this.zipcode = zipcode;
		this.stateOrProvince = stateOrProvince;
		this.country = country;
	}

	static public Builder ofCountry(String country) {
		return new Builder(country);
	}

	static public Builder from(Address address) {
		final Builder builder = new Builder(address.getCountry());
		builder.streetNumber = address.getStreetNumber();
		builder.streetName = address.getStreetName();
		builder.zipcode = address.getZipcode();
		builder.city = address.getCity();
		builder.stateOrProvince = address.getStateOrProvince();
		return builder;
	}

	public static final class Builder {

		private int streetNumber;
		private String streetName;
		private String city;
		private String zipcode;
		private String stateOrProvince;
		private String country;

		public Builder(String country) {
			Objects.requireNonNull(country, "Country can not be null.");
			this.country = country;
		}

		public Builder streetNumber(int streetNumber) {
			this.streetNumber = streetNumber;
			return this;
		}

		public Builder streetName(String streetName) {
			this.streetName = streetName;
			return this;
		}

		public Builder city(String city) {
			this.city = city;
			return this;
		}

		public Builder zipcode(String zipcode) {
			this.zipcode = zipcode;
			return this;
		}

		public Builder stateOrProvince(String stateOrProvince) {
			this.stateOrProvince = stateOrProvince;
			return this;
		}

		public Address build() {
			return new Address(streetNumber, streetName, city, zipcode, stateOrProvince, country);
		}
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getCity() {
		return city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getStateOrProvince() {
		return stateOrProvince;
	}

	public String getCountry() {
		return country;
	}
}
