package hms.model;

public class Staff extends User {

  private String role;
  private String facilityId;

  public Staff(String id, String fullName, String email) {
    super(id, fullName, email);
    this.role = "";
    this.facilityId = "";
  }

  public Staff(String id, String fullName, String email, String role, String facilityId) {
    super(id, fullName, email);
    this.role = role;
    this.facilityId = facilityId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(String facilityId) {
    this.facilityId = facilityId;
  }

  @Override
  public String toString() {
    return "Staff{id='" + getId() + "', fullName='" + getFullName() + "', email='" + getEmail() + "', role='" + role + "', facilityId='" + facilityId + "'}";
  }
}