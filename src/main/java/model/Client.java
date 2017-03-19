package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.enums.ClientType;
import model.enums.Gender;
import model.enums.MaritalStatus;
import model.enums.PhoneType;

public final class Client {

	private final String firstName;
	private final String lastName;
	private final Gender gender;
	private final LocalDate birthDate;
	private final MaritalStatus maritalStatus;
	private final Address address;
	private final Map<PhoneType, String> phones;
	private final String email;
	private final ClientType clientType;

	private Client(String firstName, String lastName, Gender gender, LocalDate birthDate, MaritalStatus maritalStatus,
			Address address, Map<PhoneType, String> phones, String email, ClientType clientType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.maritalStatus = maritalStatus;
		this.address = address;
		this.phones = phones;
		this.email = email;
		this.clientType = clientType;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public Address getAddress() {
		return address;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public Map<PhoneType, String> getPhones() {
		return Collections.unmodifiableMap(phones);
	}

	public String getEmail() {
		return email;
	}

	/**
	 * Builds a client object of the provided type.
	 * <p>
	 * If the type is {@link model.enums.ClientType#COMPANY} then the field
	 * lastName is used to store the company's name.
	 * <p>
	 * If the type is {@link model.enums.ClientType#COMPANY} then the following
	 * fields are not relevant:
	 * <p>
	 * <ul>
	 * <li>firstName</li>
	 * <li>gender</li>
	 * <li>birthDate</li>
	 * <li>maritalStatus</li>
	 * </ul>
	 * 
	 * @param clientType
	 *            The type of the client, listed in the enumeration
	 *            {@link model.enums.ClientType}
	 * @return A builder object
	 */
	static public Builder ofType(ClientType clientType) {
		return new Builder(clientType);
	}

	static public final class Builder {

		private String firstName;
		private String lastName;
		private Gender gender;
		private LocalDate birthDate;
		private MaritalStatus maritalStatus;
		private Address address;
		private Map<PhoneType, String> phones = new HashMap<>();
		private String email;
		private ClientType clientType;

		public Builder(ClientType clientType) {
			Objects.requireNonNull(clientType);
			this.clientType = clientType;
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Builder birthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public Builder maritalStatus(MaritalStatus maritalStatus) {
			this.maritalStatus = maritalStatus;
			return this;
		}

		public Builder address(Address address) {
			this.address = address;
			return this;
		}

		/**
		 * Adds a phone of the given type along with its number to a list of
		 * phones.
		 * <p>
		 * The list is backed by a map where the phone type is the key so only
		 * one number per type can exist.
		 * <p>
		 * The types are listed in the enumeration {@link PhoneType}
		 * 
		 * @param type
		 *            The type of the phone
		 * @param number
		 *            The phone number
		 */
		public Builder phone(PhoneType phonetype, String number) {
			this.phones.put(phonetype, number);
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Client build() {
			return new Client(firstName, lastName, gender, birthDate, maritalStatus, address, phones, email,
					clientType);
		}
	}

}
