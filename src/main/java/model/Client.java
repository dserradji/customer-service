package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
	private boolean estate;
	private Language language;
	private boolean solicited;
	private boolean intactEmployee;
	private Address address;
	private Map<PhoneType, String> phones = new HashMap<>();
	private String email;
	private String insuranceCompany;
	private Province InsuranceCompanyProvince;
	
	public Client() {
		
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

	/**
	 * Adds a phone of the given type with its number<br>
	 * If the phone of the same type already exists then the number is replaced with the new one<br>
	 * The types are listed in the enumeration {@link PhoneType}
	 * 
	 * @param type The type of the phone
	 * @param number The phone number
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

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public Province getInsuranceCompanyProvince() {
		return InsuranceCompanyProvince;
	}

	public void setInsuranceCompanyProvince(Province insuranceCompanyProvince) {
		InsuranceCompanyProvince = insuranceCompanyProvince;
	}
	
	
}
