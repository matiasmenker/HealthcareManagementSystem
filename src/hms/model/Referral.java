package hms.model;

import hms.model.enums.ReferralStatus;

import java.util.Objects;

public class Referral {

  private String id;
  private String patientId;
  private String referringClinicianId;
  private String referredToClinicianId;
  private String referringFacilityId;
  private String referredToFacilityId;
  private String referralDate;
  private String urgencyLevel;
  private String referralReason;
  private String clinicalSummary;
  private String requestedInvestigations;
  private ReferralStatus status;
  private String appointmentId;
  private String notes;
  private String createdDate;
  private String lastUpdated;

  public Referral(String id,
                  String patientId,
                  String referringClinicianId,
                  String referredToClinicianId,
                  String referringFacilityId,
                  String referredToFacilityId,
                  String referralDate,
                  String urgencyLevel,
                  String referralReason,
                  String clinicalSummary,
                  String requestedInvestigations,
                  ReferralStatus status,
                  String appointmentId,
                  String notes,
                  String createdDate,
                  String lastUpdated) {
    this.id = safe(id);
    this.patientId = safe(patientId);
    this.referringClinicianId = safe(referringClinicianId);
    this.referredToClinicianId = safe(referredToClinicianId);
    this.referringFacilityId = safe(referringFacilityId);
    this.referredToFacilityId = safe(referredToFacilityId);
    this.referralDate = safe(referralDate);
    this.urgencyLevel = safe(urgencyLevel);
    this.referralReason = safe(referralReason);
    this.clinicalSummary = safe(clinicalSummary);
    this.requestedInvestigations = safe(requestedInvestigations);
    this.status = status == null ? ReferralStatus.CREATED : status;
    this.appointmentId = safe(appointmentId);
    this.notes = safe(notes);
    this.createdDate = safe(createdDate);
    this.lastUpdated = safe(lastUpdated);
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

  public String getReferringClinicianId() {
    return referringClinicianId;
  }

  public void setReferringClinicianId(String referringClinicianId) {
    this.referringClinicianId = safe(referringClinicianId);
  }

  public String getReferredToClinicianId() {
    return referredToClinicianId;
  }

  public void setReferredToClinicianId(String referredToClinicianId) {
    this.referredToClinicianId = safe(referredToClinicianId);
  }

  public String getReferringFacilityId() {
    return referringFacilityId;
  }

  public void setReferringFacilityId(String referringFacilityId) {
    this.referringFacilityId = safe(referringFacilityId);
  }

  public String getReferredToFacilityId() {
    return referredToFacilityId;
  }

  public void setReferredToFacilityId(String referredToFacilityId) {
    this.referredToFacilityId = safe(referredToFacilityId);
  }

  public String getReferralDate() {
    return referralDate;
  }

  public void setReferralDate(String referralDate) {
    this.referralDate = safe(referralDate);
  }

  public String getUrgencyLevel() {
    return urgencyLevel;
  }

  public void setUrgencyLevel(String urgencyLevel) {
    this.urgencyLevel = safe(urgencyLevel);
  }

  public String getReferralReason() {
    return referralReason;
  }

  public void setReferralReason(String referralReason) {
    this.referralReason = safe(referralReason);
  }

  public String getClinicalSummary() {
    return clinicalSummary;
  }

  public void setClinicalSummary(String clinicalSummary) {
    this.clinicalSummary = safe(clinicalSummary);
  }

  public String getRequestedInvestigations() {
    return requestedInvestigations;
  }

  public void setRequestedInvestigations(String requestedInvestigations) {
    this.requestedInvestigations = safe(requestedInvestigations);
  }

  public ReferralStatus getStatus() {
    return status;
  }

  public void setStatus(ReferralStatus status) {
    this.status = status == null ? ReferralStatus.CREATED : status;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = safe(appointmentId);
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = safe(notes);
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = safe(createdDate);
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = safe(lastUpdated);
  }

  @Override
  public String toString() {
    return "Referral{id='" + safe(id) + "', patientId='" + safe(patientId) + "', status='" + (status == null ? "" : status.name()) + "'}";
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