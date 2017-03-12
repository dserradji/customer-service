package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import model.enums.ClientType;
import model.enums.Gender;
import model.enums.Language;
import model.enums.MaritalStatus;
import model.enums.NameSuffix;
import model.enums.PhoneType;
import model.enums.Province;

public class Client {

	private String firstName;
	private String lastName;
	private NameSuffix suffix;
	private Gender gender;
	private LocalDate birthDate;
	private MaritalStatus maritalStatus;
	private boolean estate = false;
	private Language language;
	private boolean solicited;
	private boolean intactEmployee;
	private Address address;
	private Map<PhoneType, String> phones = new HashMap<>();
	private String email;
	private InsuranceCompany insuranceCompany;
	private Province InsuranceCompanyProvince;
	private ClientType clientType;

	private Client() {

	}

	public NameSuffix getSuffix() {
		return suffix;
	}

	public void setSuffix(NameSuffix suffix) {
		this.suffix = suffix;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public boolean isEstate() {
		return estate;
	}

	public void setEstate(boolean estate) {
		this.estate = estate;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isSolicited() {
		return solicited;
	}

	public void setSolicited(boolean solicited) {
		this.solicited = solicited;
	}

	public boolean isIntactEmployee() {
		return intactEmployee;
	}

	public void setIntactEmployee(boolean intactEmployee) {
		this.intactEmployee = intactEmployee;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	/**
	 * Adds a phone of the given type with its number<br>
	 * If the phone of the same type already exists then the number is replaced
	 * with the new one<br>
	 * The types are listed in the enumeration {@link PhoneType}
	 * 
	 * @param type
	 *            The type of the phone
	 * @param number
	 *            The phone number
	 */
	public void addPhone(PhoneType type, String number) {
		phones.put(type, number);

	}

	public String getPhoneNumber(PhoneType type) {
		return phones.get(type);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public InsuranceCompany getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public Province getInsuranceCompanyProvince() {
		return InsuranceCompanyProvince;
	}

	public void setInsuranceCompanyProvince(Province insuranceCompanyProvince) {
		InsuranceCompanyProvince = insuranceCompanyProvince;
	}

	/**
	 * Builds a client object of the provided type.
	 * <p>
	 * When the type is {@link model.enums.ClientType#COMPANY} then the field
	 * lastName is used to store the company's name.
	 * <p>
	 * When the type is {@link model.enums.ClientType#COMPANY} then the
	 * following fields are not relevant:
	 * <p>
	 * <ul>
	 * <li>firstName</li>
	 * <li>suffix</li>
	 * <li>gender</li>
	 * <li>birthDate</li>
	 * <li>maritalStatus</li>
	 * <li>estate</li>
	 * </ul>
	 * 
	 * @param clientType
	 *            The type of the client listed in the enumeration
	 *            {@link model.enums.ClientType}
	 * @return A builder object, the method {@link model.Client.Builder#build} returns
	 *         an implementation of Client depending on the provided type
	 */
	static public Builder ofType(ClientType clientType) {
		return new Builder(clientType);
	}

	static final class Builder {

		private final Client client;

		public Builder(ClientType clientType) {
			client = new Client();
			client.setClientType(clientType);
		}

		public Builder withFirstName(String firstName) {
			client.setFirstName(firstName);
			return this;
		}

		public Builder withLastName(String lastName) {
			client.setLastName(lastName);
			return this;
		}

		public Builder withSuffix(NameSuffix suffix) {
			client.setSuffix(suffix);
			return this;
		}

		public Builder withGender(Gender gender) {
			client.setGender(gender);
			return this;
		}

		public Builder withBirthDate(LocalDate birthDate) {
			client.setBirthDate(birthDate);
			return this;
		}

		public Builder withMaritalStatus(MaritalStatus maritalStatus) {
			client.setMaritalStatus(maritalStatus);
			return this;
		}

		public Builder isEstate(Boolean isEstate) {
			client.setEstate(isEstate);
			return this;
		}

		public Builder withLanguage(Language language) {
			client.setLanguage(language);
			return this;
		}

		public Builder isSolicited(boolean isSolicited) {
			client.setSolicited(isSolicited);
			return this;
		}

		public Builder isIntactEmployee(boolean isIntactemployee) {
			client.setIntactEmployee(isIntactemployee);
			return this;
		}

		public Builder withAddress(Address address) {
			client.setAddress(address);
			return this;
		}

		public Builder withPhone(PhoneType phonetype, String number) {
			client.addPhone(phonetype, number);
			return this;
		}

		public Builder withEmail(String email) {
			client.setEmail(email);
			return this;
		}

		public Builder withInsuranceCompany(InsuranceCompany insuranceCompany) {
			client.setInsuranceCompany(insuranceCompany);
			return this;
		}

		public Builder withInsuranceCompanyProvince(Province province) {
			client.setInsuranceCompanyProvince(province);
			return this;
		}

		public Client build() {
			return client;
		}
	}

}
