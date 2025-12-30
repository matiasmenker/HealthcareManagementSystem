package hms.model;

public class Clinician extends User {

  private String role;
  private String qualification;
  private String facilityId;

  public Clinician(String id, String fullName, String email) {
    super(id, fullName, email);
    this.role = "";
    this.qualification = "";
    this.facilityId = "";
  }

  public Clinician(String id,
                   String fullName,
                   String email,
                   String role,
                   String qualification,
                   String facilityId) {
    super(id, fullName, email);
    this.role = role;
    this.qualification = qualification;
    this.facilityId = facilityId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public String getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(String facilityId) {
    this.facilityId = facilityId;
  }

  @Override
  public String toString() {
    return "Clinician{id='" + getId()
        + "', fullName='" + getFullName()
        + "', email='" + getEmail()
        + "', role='" + role
        + "', qualification='" + qualification
        + "', facilityId='" + facilityId
        + "'}";
  }
}