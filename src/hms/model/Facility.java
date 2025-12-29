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
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.postcode = postcode;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.openingHours = openingHours;
		this.managerName = managerName;
		this.capacity = capacity;
		this.specialitiesOffered = specialitiesOffered;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getSpecialitiesOffered() {
		return specialitiesOffered;
	}

	public void setSpecialitiesOffered(String specialitiesOffered) {
		this.specialitiesOffered = specialitiesOffered;
	}

	@Override
	public String toString() {
		return "Facility{id='" + id + "', name='" + name + "', type='" + type + "', address='" + address
				+ "', postcode='" + postcode + "', phoneNumber='" + phoneNumber + "', email='" + email
				+ "', openingHours='" + openingHours + "', managerName='" + managerName + "', capacity='" + capacity
				+ "', specialitiesOffered='" + specialitiesOffered + "'}";
	}
}