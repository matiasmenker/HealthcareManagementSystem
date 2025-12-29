package hms.model;

public class Staff {

	private String id;
	private String fullName;
	private String email;
	private String role;
	private String department;
	private String phoneNumber;
	private String workLocation;
	private String shiftPattern;
	private String employmentStatus;
	private String startDate;
	private String supervisorName;
	private String accessLevel;

	public Staff(String id, String fullName, String email, String role, String department, String phoneNumber,
			String workLocation, String shiftPattern, String employmentStatus, String startDate, String supervisorName,
			String accessLevel) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.role = role;
		this.department = department;
		this.phoneNumber = phoneNumber;
		this.workLocation = workLocation;
		this.shiftPattern = shiftPattern;
		this.employmentStatus = employmentStatus;
		this.startDate = startDate;
		this.supervisorName = supervisorName;
		this.accessLevel = accessLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getShiftPattern() {
		return shiftPattern;
	}

	public void setShiftPattern(String shiftPattern) {
		this.shiftPattern = shiftPattern;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Override
	public String toString() {
		return "Staff{id='" + id + "', fullName='" + fullName + "', email='" + email + "', role='" + role
				+ "', department='" + department + "', phoneNumber='" + phoneNumber + "', workLocation='" + workLocation
				+ "', shiftPattern='" + shiftPattern + "', employmentStatus='" + employmentStatus + "', startDate='"
				+ startDate + "', supervisorName='" + supervisorName + "', accessLevel='" + accessLevel + "'}";
	}
}