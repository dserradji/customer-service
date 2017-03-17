package clientservice.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clientservice.models.enums.AddressType;
import clientservice.models.enums.Province;
import clientservice.models.enums.State;

public interface Address {

	static final String NOT_SUPPORTED = "Method not supported for %s addresses";

	// Canada
	default int getStreetNumber() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getStreetName() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default Province getProvince() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getPostalCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// USA
	default String getAddressLine() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default State getState() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default int getZipCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// Other countries
	default List<String> getLines() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// Shared
	default String getCity() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getCountry() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	public AddressType getType();

	// Builders
	static BuilderCanada ofCanada() {
		return new BuilderCanada();
	}

	static BuilderUSA ofUSA() {
		return new BuilderUSA();
	}

	static BuilderOthers ofOthers() {
		return new BuilderOthers();
	}
	
	static final class BuilderCanada {

		private int streetNumber;
		private String streetName;
		private Province province;
		private String postalCode;
		private String city;

		public BuilderCanada streetNumber(int streetNumber) {
			this.streetNumber = streetNumber;
			return this;
		}

		public BuilderCanada streetName(String streetName) {
			this.streetName = streetName;
			return this;
		}

		public BuilderCanada province(Province province) {
			this.province = province;
			return this;
		}

		public BuilderCanada postalCode(String postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		public BuilderCanada city(String city) {
			this.city = city;
			return this;
		}

		public Address build() {
			return new Address.Canada(streetNumber, streetName, province, postalCode, city);
		}
	}

	static final class BuilderUSA {

		private String addressLine;
		private State state;
		private int zipCode;
		private String city;

		public BuilderUSA addressLine(String addressLine) {
			this.addressLine = addressLine;
			return this;
		}

		public BuilderUSA state(State state) {
			this.state = state;
			return this;
		}

		public BuilderUSA zipCode(int zipCode) {
			this.zipCode = zipCode;
			return this;
		}

		public BuilderUSA city(String city) {
			this.city = city;
			return this;
		}

		public Address build() {
			return new Address.USA(addressLine, state, zipCode, city);
		}
		
	}

	static final class BuilderOthers {
		
		private List<String> lines = new ArrayList<>();
		private String country;
		
		public BuilderOthers withLine(String line) {
			this.lines.add(line);
			return this;
		}

		public BuilderOthers country(String country) {
			this.country = country;
			return this;
		}

		public Address build() {
			return new Address.Others(lines, country);
		}
	}
	
	static final class Canada implements Address {

		private final int streetNumber;
		private final String streetName;
		private final Province province;
		private final String postalCode;
		private final String city;
		private final String country;
		private final AddressType addressType;

		private Canada(int streetNumber, String streetName, Province province, String postalCode, String city) {

			this.streetNumber = streetNumber;
			this.streetName = streetName;
			this.province = province;
			this.postalCode = postalCode;
			this.city = city;

			this.addressType = AddressType.CANADA;
			this.country = addressType.getName();
		}

		@Override
		public int getStreetNumber() {
			return streetNumber;
		}

		@Override
		public String getStreetName() {
			return streetName;
		}

		@Override
		public Province getProvince() {
			return province;
		}

		@Override
		public String getPostalCode() {
			return postalCode;
		}

		@Override
		public String getCity() {
			return city;
		}

		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
	}

	static final class USA implements Address {

		private final String addressLine;
		private final State state;
		private final int zipCode;
		private final String city;
		private final String country;
		private final AddressType addressType;

		private USA(String addressLine, State state, int zipCode, String city) {

			this.addressLine = addressLine;
			this.state = state;
			this.zipCode = zipCode;
			this.city = city;

			this.addressType = AddressType.USA;
			this.country = this.addressType.getName();
		}

		@Override
		public String getAddressLine() {
			return addressLine;
		}

		@Override
		public State getState() {
			return state;
		}

		@Override
		public int getZipCode() {
			return zipCode;
		}

		@Override
		public String getCity() {
			return city;
		}

		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
	}

	static final class Others implements Address {

		private final List<String> lines;
		private final String country;
		private final AddressType addressType;

		private Others(List<String> lines, String country) {
			this.lines = lines;
			this.country = country;
			this.addressType = AddressType.OTHERS;
		}

		@Override
		public List<String> getLines() {
			return Collections.unmodifiableList(lines);
		}

		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
	}
}
