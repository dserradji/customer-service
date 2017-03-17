package clientservice.models.enums;

public enum AddressType {

	CANADA("Canada"), USA("USA"), OTHERS("Others");

	private String name;

	private AddressType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
