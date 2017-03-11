package model;

import java.util.List;

import model.enums.Country;
import model.enums.Province;
import model.enums.State;

public interface Address {

	static final String NOT_SUPPORTED = "Method not supported for %s addresses";

	// Canada
	default public int getStreetNumber() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setStreetNumber(int streetNumber) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public String getStreetName() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setStreetName(String streetName) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public Province getProvince() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setProvince(Province province) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public String getPostalCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setPostalCode(String postalCode) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	// USA
	default public String getAddress() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setAddress(String address) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public State getState() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setState(State state) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public int getZipCode() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setZipCode(int zipCode) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	// Other countries
	default public List<String> getLines() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setLines(List<String> lines) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	// Shared
	default public String getCity() {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	default public void setCity(String city) {
		throw new UnsupportedOperationException(String.format(NOT_SUPPORTED, getCountry()));
	}

	public Country getCountry();
	
	public void setCountry(Country country);
	
	// Build the address according to the country
	public static Builder of(Country country) {
		return new Builder(country);
	}

	static final class Builder {

		private final Address address;

		private Builder(Country country) {
			switch (country) {
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
						String.format("%s is not supported yet, please provide an implemetation", country));
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

		public Builder lines(List<String> lines) {
			address.setLines(lines);
			return this;
		}

		public Address build() {
			return address;
		}
	}

	static class Canada implements Address {

		private int streetNumber;
		private String streetName;
		private Province province;
		private String postalCode;
		private String city;
		private Country country;

		private Canada() {
			country = Country.CANADA;
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
		
		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		@Override
		public Country getCountry() {
			return country;
		}
		
		@Override
		public void setCountry(Country country) {
			this.country = country;
		}
	}

	static class USA implements Address {

		private String address;
		private State state;
		private int zipCode;
		private String city;
		private Country country;

		private USA() {
			country = Country.USA;
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
		public Country getCountry() {
			return country;
		}
		
		@Override
		public void setCountry(Country country) {
			this.country = country;
		}
	}

	static class Others implements Address {

		private List<String> lines;
		private Country country;

		public Others() {
			country = Country.OTHERS;
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
		public Country getCountry() {
			return country;
		}
		
		@Override
		public void setCountry(Country country) {
			this.country = country;
		}
	}
}
