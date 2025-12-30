package hms.model;

import java.util.Objects;

public class Referral {

  private String id;
  private String patientId;
  private String fromClinicianId;
  private String toClinicianId;
  private String fromFacilityId;
  private String toFacilityId;
  private String urgency;
  private String clinicalSummary;
  private String status;
  private String dateCreated;

  public Referral(String id,
                  String patientId,
                  String fromClinicianId,
                  String toClinicianId,
                  String fromFacilityId,
                  String toFacilityId,
                  String urgency,
                  String clinicalSummary,
                  String status,
                  String dateCreated) {
    this.id = safe(id);
    this.patientId = safe(patientId);
    this.fromClinicianId = safe(fromClinicianId);
    this.toClinicianId = safe(toClinicianId);
    this.fromFacilityId = safe(fromFacilityId);
    this.toFacilityId = safe(toFacilityId);
    this.urgency = safe(urgency);
    this.clinicalSummary = safe(clinicalSummary);
    this.status = safe(status);
    this.dateCreated = safe(dateCreated);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = safe(id);
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = safe(patientId);
  }

  public String getFromClinicianId() {
    return fromClinicianId;
  }

  public void setFromClinicianId(String fromClinicianId) {
    this.fromClinicianId = safe(fromClinicianId);
  }

  public String getToClinicianId() {
    return toClinicianId;
  }

  public void setToClinicianId(String toClinicianId) {
    this.toClinicianId = safe(toClinicianId);
  }

  public String getFromFacilityId() {
    return fromFacilityId;
  }

  public void setFromFacilityId(String fromFacilityId) {
    this.fromFacilityId = safe(fromFacilityId);
  }

  public String getToFacilityId() {
    return toFacilityId;
  }

  public void setToFacilityId(String toFacilityId) {
    this.toFacilityId = safe(toFacilityId);
  }

  public String getUrgency() {
    return urgency;
  }

  public void setUrgency(String urgency) {
    this.urgency = safe(urgency);
  }

  public String getClinicalSummary() {
    return clinicalSummary;
  }

  public void setClinicalSummary(String clinicalSummary) {
    this.clinicalSummary = safe(clinicalSummary);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = safe(status);
  }

  public String getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = safe(dateCreated);
  }

  public String getCreatedDate() {
    return getDateCreated();
  }

  public void setCreatedDate(String createdDate) {
    setDateCreated(createdDate);
  }

  public String getReferralReason() {
    return getClinicalSummary();
  }

  public void setReferralReason(String referralReason) {
    setClinicalSummary(referralReason);
  }

  @Override
  public String toString() {
    return "Referral{id='" + safe(id) + "', patientId='" + safe(patientId) + "', fromClinicianId='" + safe(fromClinicianId)
        + "', toClinicianId='" + safe(toClinicianId) + "', fromFacilityId='" + safe(fromFacilityId) + "', toFacilityId='"
        + safe(toFacilityId) + "', urgency='" + safe(urgency) + "', status='" + safe(status) + "', dateCreated='" + safe(dateCreated) + "'}";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Referral)) {
      return false;
    }
    Referral referral = (Referral) other;
    return Objects.equals(safe(id), safe(referral.id));
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