package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.AddressType;
import model.enums.Province;
import model.enums.State;

public interface Address {

	static final String NOT_SUPPORTED = "Method not supported for %s addresses";

	// Canada
	default int getStreetNumber() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setStreetNumber(int streetNumber) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getStreetName() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setStreetName(String streetName) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default Province getProvince() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setProvince(Province province) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getPostalCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setPostalCode(String postalCode) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// USA
	default String getAddress() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setAddress(String address) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default State getState() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setState(State state) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default int getZipCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setZipCode(int zipCode) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// Other countries
	default List<String> getLines() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setLines(List<String> lines) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	// Shared
	default String getCity() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setCity(String city) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default String getCountry() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	default void setCountry(String country) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getType()));
	}

	public AddressType getType();
	
	public void setType(AddressType addressType);
	
	// Build the address according to the country
	public static Builder of(AddressType country) {
		return new Builder(country);
	}

	static final class Builder {

		private final Address address;

		private Builder(AddressType addressType) {
			switch (addressType) {
			case CANADA:
				address = new Address.Canada();
				break;
			case USA:
				address = new Address.USA();
				break;
			case OTHERS:
				address = new Address.Others();
				break;
			default:
				throw new IllegalArgumentException(
						String.format("%s is not supported yet, please provide an implemetation", addressType));
			}
		}

		public Builder streetNumber(int streetNumber) {
			address.setStreetNumber(streetNumber);
			return this;
		}

		public Builder streetName(String streetName) {
			address.setStreetName(streetName);
			return this;
		}

		public Builder province(Province province) {
			address.setProvince(province);
			return this;
		}

		public Builder postalCode(String postalCode) {
			address.setPostalCode(postalCode);
			return this;
		}

		public Builder address(String address) {
			this.address.setAddress(address);
			return this;
		}

		public Builder state(State state) {
			address.setState(state);
			return this;
		}

		public Builder zipCode(int zipCode) {
			address.setZipCode(zipCode);
			return this;
		}

		public Builder city(String city) {
			address.setCity(city);
			return this;
		}

		public Builder country(String country) {
			address.setCountry(country);
			return this;
		}

		public Builder withLine(String line) {
			address.getLines().add(line);
			return this;
		}

		public Address build() {
			return address;
		}
	}

	static class Canada implements Address {

		private int streetNumber = Integer.MIN_VALUE;
		private String streetName;
		private Province province;
		private String postalCode;
		private String city;
		private String country;
		private AddressType addressType;

		private Canada() {
			addressType = AddressType.CANADA;
			country = addressType.getName();
		}

		@Override
		public int getStreetNumber() {
			return streetNumber;
		}

		@Override
		public void setStreetNumber(int streetNumber) {
			this.streetNumber = streetNumber;
		}

		@Override
		public String getStreetName() {
			return streetName;
		}

		@Override
		public void setStreetName(String streetName) {
			this.streetName = streetName;
		}

		@Override
		public Province getProvince() {
			return province;
		}

		@Override
		public void setProvince(Province province) {
			this.province = province;
		}

		@Override
		public String getPostalCode() {
			return postalCode;
		}

		@Override
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		
		@Override
		public String getCity() {
			return city;
		}

		@Override
		public void setCity(String city) {
			this.city = city;
		}
		
		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
		
		@Override
		public void setType(AddressType country) {
			this.addressType = country;
		}
	}

	static class USA implements Address {

		private String address;
		private State state;
		private int zipCode = Integer.MIN_VALUE;
		private String city;
		private String country;
		private AddressType addressType;

		private USA() {
			addressType = AddressType.USA;
			country = addressType.getName();
		}

		@Override
		public String getAddress() {
			return address;
		}

		@Override
		public void setAddress(String address) {
			this.address = address;
		}
		
		@Override
		public State getState() {
			return state;
		}

		@Override
		public void setState(State state) {
			this.state = state;
		}

		@Override
		public int getZipCode() {
			return zipCode;
		}

		@Override
		public void setZipCode(int zipCode) {
			this.zipCode = zipCode;
		}
		
		@Override
		public String getCity() {
			return city;
		}

		@Override
		public void setCity(String city) {
			this.city = city;
		}
		
		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
		
		@Override
		public void setType(AddressType addressType) {
			this.addressType = addressType;
		}
	}

	static class Others implements Address {

		private List<String> lines;
		private String country;
		private AddressType addressType;

		private Others() {
			lines = new ArrayList<>();
			addressType = AddressType.OTHERS;
		}
		
		@Override
		public List<String> getLines() {
			return lines;
		}

		@Override
		public void setLines(List<String> lines) {
			this.lines = lines;
		}
		
		@Override
		public String getCountry() {
			return country;
		}

		@Override
		public void setCountry(String country) {
			this.country = country;
		}

		@Override
		public AddressType getType() {
			return addressType;
		}
		
		@Override
		public void setType(AddressType addressType) {
			this.addressType = addressType;
		}
	}
}
