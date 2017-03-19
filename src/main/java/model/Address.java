package model;

import java.util.Objects;

public final class Address {

	private final int streetNumber;
	private final String streetName;
	private final String city;
	private final String zipcode;
	private final String stateOrProvince;
	private final String country;

	
	private Address(int streetNumber, String streetName, String city, String zipcode, String stateOrProvince, String country) {
		this.streetNumber = streetNumber;
		this.streetName = streetName;
		this.city = city;
		this.zipcode = zipcode;
		this.stateOrProvince = stateOrProvince;
		this.country = country;
	}

	static Builder ofCountry(String country) {
		return new Builder(country);
	}

	static final class Builder {

		private int streetNumber;
		private String streetName;
		private String city;
		private String zipcode;
		private String stateOrProvince;
		private String country;

		public Builder(String country) {
			Objects.requireNonNull(country);
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
