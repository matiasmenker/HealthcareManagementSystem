package hms.model;

import java.util.Objects;

public class Clinician {

  private String id;
  private String firstName;
  private String lastName;
  private String title;
  private String speciality;
  private String gmcNumber;
  private String phoneNumber;
  private String email;
  private String workplaceId;
  private String workplaceType;
  private String employmentStatus;
  private String startDate;

  public Clinician(String id,
                   String firstName,
                   String lastName,
                   String title,
                   String speciality,
                   String gmcNumber,
                   String phoneNumber,
                   String email,
                   String workplaceId,
                   String workplaceType,
                   String employmentStatus,
                   String startDate) {
    this.id = safe(id);
    this.firstName = safe(firstName);
    this.lastName = safe(lastName);
    this.title = safe(title);
    this.speciality = safe(speciality);
    this.gmcNumber = safe(gmcNumber);
    this.phoneNumber = safe(phoneNumber);
    this.email = safe(email);
    this.workplaceId = safe(workplaceId);
    this.workplaceType = safe(workplaceType);
    this.employmentStatus = safe(employmentStatus);
    this.startDate = safe(startDate);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = safe(id);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = safe(firstName);
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = safe(lastName);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = safe(title);
  }

  public String getSpeciality() {
    return speciality;
  }

  public void setSpeciality(String speciality) {
    this.speciality = safe(speciality);
  }

  public String getSpecialty() {
    return getSpeciality();
  }

  public void setSpecialty(String specialty) {
    setSpeciality(specialty);
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = safe(gmcNumber);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = safe(phoneNumber);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = safe(email);
  }

  public String getWorkplaceId() {
    return workplaceId;
  }

  public void setWorkplaceId(String workplaceId) {
    this.workplaceId = safe(workplaceId);
  }

  public String getFacilityId() {
    return getWorkplaceId();
  }

  public void setFacilityId(String facilityId) {
    setWorkplaceId(facilityId);
  }

  public String getWorkplaceType() {
    return workplaceType;
  }

  public void setWorkplaceType(String workplaceType) {
    this.workplaceType = safe(workplaceType);
  }

  public String getEmploymentStatus() {
    return employmentStatus;
  }

  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = safe(employmentStatus);
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = safe(startDate);
  }

  public String getFullName() {
    String combined = (safe(firstName) + " " + safe(lastName)).trim();
    return combined;
  }

  @Override
  public String toString() {
    return "Clinician{id='" + safe(id)
        + "', firstName='" + safe(firstName)
        + "', lastName='" + safe(lastName)
        + "', title='" + safe(title)
        + "', speciality='" + safe(speciality)
        + "', workplaceId='" + safe(workplaceId)
        + "'}";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Clinician)) {
      return false;
    }
    Clinician clinician = (Clinician) other;
    return Objects.equals(safe(id), safe(clinician.id));
  }

  @Override
  public int hashCode() {
    return Objects.hash(safe(id));
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}