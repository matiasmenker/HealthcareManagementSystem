package hms.model;

public class Facility {

	private String id;
	private String name;
	private String type;
	private String address;
	private String postcode;
	private String phoneNumber;
	private String email;
	private String openingHours;
	private String managerName;
	private String capacity;
	private String specialitiesOffered;

	public Facility(String id, String name, String type, String address, String postcode, String phoneNumber,
			String email, String openingHours, String managerName, String capacity, String specialitiesOffered) {
		this.id = safe(id);
		this.name = safe(name);
		this.type = safe(type);
		this.address = safe(address);
		this.postcode = safe(postcode);
		this.phoneNumber = safe(phoneNumber);
		this.email = safe(email);
		this.openingHours = safe(openingHours);
		this.managerName = safe(managerName);
		this.capacity = safe(capacity);
		this.specialitiesOffered = safe(specialitiesOffered);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = safe(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = safe(name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = safe(type);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = safe(address);
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = safe(postcode);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = safe(phoneNumber);
	}

	public String getPhone() {
		return getPhoneNumber();
	}

	public void setPhone(String phone) {
		setPhoneNumber(phone);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = safe(email);
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = safe(openingHours);
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = safe(managerName);
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = safe(capacity);
	}

	public String getSpecialitiesOffered() {
		return specialitiesOffered;
	}

	public void setSpecialitiesOffered(String specialitiesOffered) {
		this.specialitiesOffered = safe(specialitiesOffered);
	}

	@Override
	public String toString() {
		return "Facility{id='" + safe(id) + "', name='" + safe(name) + "', type='" + safe(type) + "', address='"
				+ safe(address) + "', postcode='" + safe(postcode) + "', phoneNumber='" + safe(phoneNumber)
				+ "', email='" + safe(email) + "', openingHours='" + safe(openingHours) + "', managerName='"
				+ safe(managerName) + "', capacity='" + safe(capacity) + "', specialitiesOffered='"
				+ safe(specialitiesOffered) + "'}";
	}

	private String safe(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}