package hms.model;

public class Patient extends User {
	private String nhsNumber;
	private String phone;
	private String address;
	private String registeredFacilityId;

	public Patient(String id, String fullName, String email, String nhsNumber, String phone, String address,
			String registeredFacilityId) {
		super(id, fullName, email);
		this.nhsNumber = nhsNumber;
		this.phone = phone;
		this.address = address;
		this.registeredFacilityId = registeredFacilityId;
	}

	public String getNhsNumber() {
		return nhsNumber;
	}

	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegisteredFacilityId() {
		return registeredFacilityId;
	}

	public void setRegisteredFacilityId(String registeredFacilityId) {
		this.registeredFacilityId = registeredFacilityId;
	}

	@Override
	public String toString() {
		return "Patient{id='" + getId() + "', fullName='" + getFullName() + "', email='" + getEmail() + "', nhsNumber='"
				+ nhsNumber + "', phone='" + phone + "', address='" + address + "', registeredFacilityId='"
				+ registeredFacilityId + "'}";
	}
}