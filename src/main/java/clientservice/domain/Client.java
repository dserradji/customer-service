package clientservice.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import clientservice.ClientServiceException;
import clientservice.domain.enums.ClientType;
import clientservice.domain.enums.Gender;
import clientservice.domain.enums.MaritalStatus;
import clientservice.domain.enums.PhoneType;

/**
 * {@code Client} is a immutable object.
 * <p>
 * Use {@code Client.ofType(ClientType)} builder to create a new client.
 * <p>
 * Use {@code Client.from(myClient)} builder to make a copy of myClient then
 * update its fields with one of the {@code with*()} methods.<br>
 * <p>
 * Example:<br>
 * {@code Client myClient = Client.ofType(ClientType.PERSON).firstName("Ken").build();}<br>
 * {@code Client myClient = Client.from(myClient).firstName("Bison").build();}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public final class Client {

	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId id;
	private String firstName;
	private String lastName;
	private Gender gender;
	@JsonFormat(shape = Shape.STRING)
	private LocalDate birthDate;
	private MaritalStatus maritalStatus;
	private Address address;
	private Map<PhoneType, String> phones;
	private String email;
	@NotNull
	private ClientType clientType;

	private Client() {
	}

	private Client(ObjectId id, String firstName, String lastName, Gender gender, LocalDate birthDate,
			MaritalStatus maritalStatus, Address address, Map<PhoneType, String> phones, String email,
			ClientType clientType) {
		this.id = id;
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

	public ObjectId getId() {
		return id;
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
		
		if (phones == null) {
			return phones;
		}
		
		final Map<PhoneType, String> copie = new HashMap<>();
		phones.forEach((key, value) -> copie.put(key, value));
		return copie;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * Builds a client object of the provided type.
	 * <p>
	 * If the type is {@link clientservice.domain.enums.ClientType#COMPANY} then
	 * the field lastName is used to store the company's name.
	 * <p>
	 * If the type is {@link clientservice.domain.enums.ClientType#COMPANY} then
	 * the following fields are not relevant:
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
	 *            {@link clientservice.domain.enums.ClientType}
	 * @return A builder object
	 */
	static public Builder ofType(ClientType clientType) {
		return new Builder(clientType);
	}

	static public Builder from(Client client) {

		final Builder builder = new Builder(client.clientType);
		builder.id = client.id;
		builder.firstName = client.firstName;
		builder.lastName = client.lastName;
		builder.gender = client.gender;
		builder.maritalStatus = client.maritalStatus;
		builder.address = client.address;
		builder.birthDate = client.birthDate;
		builder.email = client.email;
		builder.phones = client.getPhones(); // we need a copy of the map hence
												// the getter
		return builder;
	}

	static public final class Builder {

		private ObjectId id;
		private String firstName;
		private String lastName;
		private Gender gender;
		private LocalDate birthDate;
		private MaritalStatus maritalStatus;
		private Address address;
		private Map<PhoneType, String> phones;
		private String email;
		private ClientType clientType;

		public Builder(ClientType clientType) {
			if (clientType == null) {
				throw new ClientServiceException(HttpStatus.BAD_REQUEST, "Client type can not be null.");
			}
			this.clientType = clientType;
		}

		public Builder withId(ObjectId id) {
			this.id = id;
			return this;
		}

		public Builder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder withGender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Builder withBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public Builder withMaritalStatus(MaritalStatus maritalStatus) {
			this.maritalStatus = maritalStatus;
			return this;
		}

		public Builder withAddress(Address address) {
			this.address = address;
			return this;
		}

		/**
		 * Add a phone to the client's list of phones.<br>
		 * A phone consists of a a pair of (phone type, phone number).
		 * <p>
		 * The list is backed by a map where the phone type is the key so only
		 * one number per type can exist.
		 * <p>
		 * The available types are listed in the enumeration {@link PhoneType}
		 * 
		 * @param type
		 *            The type of the phone
		 * @param number
		 *            The phone number
		 */
		public Builder withPhone(PhoneType phonetype, String number) {
			
			if (phones == null) {
				phones = new HashMap<>();
			}
			
			this.phones.put(phonetype, number);
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Client build() {
			return new Client(id, firstName, lastName, gender, birthDate, maritalStatus, address, phones, email,
					clientType);
		}
	}
}
