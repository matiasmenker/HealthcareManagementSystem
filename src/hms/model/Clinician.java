package hms.model;

public class Clinician {

  private String id;
  private String fullName;
  private String email;
  private String title;
  private String specialty;
  private String medicalRegistrationNumber;
  private String phoneNumber;
  private String workplaceId;
  private String workplaceType;
  private String employmentStatus;
  private String startDate;

  public Clinician(String id,
                   String fullName,
                   String email,
                   String title,
                   String specialty,
                   String medicalRegistrationNumber,
                   String phoneNumber,
                   String workplaceId,
                   String workplaceType,
                   String employmentStatus,
                   String startDate) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.title = title;
    this.specialty = specialty;
    this.medicalRegistrationNumber = medicalRegistrationNumber;
    this.phoneNumber = phoneNumber;
    this.workplaceId = workplaceId;
    this.workplaceType = workplaceType;
    this.employmentStatus = employmentStatus;
    this.startDate = startDate;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getMedicalRegistrationNumber() {
    return medicalRegistrationNumber;
  }

  public void setMedicalRegistrationNumber(String medicalRegistrationNumber) {
    this.medicalRegistrationNumber = medicalRegistrationNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getWorkplaceId() {
    return workplaceId;
  }

  public void setWorkplaceId(String workplaceId) {
    this.workplaceId = workplaceId;
  }

  public String getWorkplaceType() {
    return workplaceType;
  }

  public void setWorkplaceType(String workplaceType) {
    this.workplaceType = workplaceType;
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

  @Override
  public String toString() {
    return "Clinician{id='" + id
        + "', fullName='" + fullName
        + "', email='" + email
        + "', title='" + title
        + "', specialty='" + specialty
        + "', medicalRegistrationNumber='" + medicalRegistrationNumber
        + "', phoneNumber='" + phoneNumber
        + "', workplaceId='" + workplaceId
        + "', workplaceType='" + workplaceType
        + "', employmentStatus='" + employmentStatus
        + "', startDate='" + startDate
        + "'}";
  }
}