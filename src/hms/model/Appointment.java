package hms.model;

import hms.model.enums.AppointmentStatus;

public class Appointment {
  private String id;
  private String patientId;
  private String clinicianId;
  private String facilityId;
  private String dateTime;
  private AppointmentStatus status;
  private String reason;

  public Appointment(String id,
                     String patientId,
                     String clinicianId,
                     String facilityId,
                     String dateTime,
                     AppointmentStatus status,
                     String reason) {
    this.id = id;
    this.patientId = patientId;
    this.clinicianId = clinicianId;
    this.facilityId = facilityId;
    this.dateTime = dateTime;
    this.status = status;
    this.reason = reason;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getClinicianId() {
    return clinicianId;
  }

  public void setClinicianId(String clinicianId) {
    this.clinicianId = clinicianId;
  }

  public String getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(String facilityId) {
    this.facilityId = facilityId;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return "Appointment{id='" + id
        + "', patientId='" + patientId
        + "', clinicianId='" + clinicianId
        + "', facilityId='" + facilityId
        + "', dateTime='" + dateTime
        + "', status='" + status
        + "', reason='" + reason
        + "'}";
  }
}