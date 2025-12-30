package hms.model;

import hms.model.enums.ReferralStatus;

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
    this.id = id;
    this.patientId = patientId;
    this.referringClinicianId = referringClinicianId;
    this.referredToClinicianId = referredToClinicianId;
    this.referringFacilityId = referringFacilityId;
    this.referredToFacilityId = referredToFacilityId;
    this.referralDate = referralDate;
    this.urgencyLevel = urgencyLevel;
    this.referralReason = referralReason;
    this.clinicalSummary = clinicalSummary;
    this.requestedInvestigations = requestedInvestigations;
    this.status = status;
    this.appointmentId = appointmentId;
    this.notes = notes;
    this.createdDate = createdDate;
    this.lastUpdated = lastUpdated;
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

  public String getReferringClinicianId() {
    return referringClinicianId;
  }

  public void setReferringClinicianId(String referringClinicianId) {
    this.referringClinicianId = referringClinicianId;
  }

  public String getReferredToClinicianId() {
    return referredToClinicianId;
  }

  public void setReferredToClinicianId(String referredToClinicianId) {
    this.referredToClinicianId = referredToClinicianId;
  }

  public String getReferringFacilityId() {
    return referringFacilityId;
  }

  public void setReferringFacilityId(String referringFacilityId) {
    this.referringFacilityId = referringFacilityId;
  }

  public String getReferredToFacilityId() {
    return referredToFacilityId;
  }

  public void setReferredToFacilityId(String referredToFacilityId) {
    this.referredToFacilityId = referredToFacilityId;
  }

  public String getReferralDate() {
    return referralDate;
  }

  public void setReferralDate(String referralDate) {
    this.referralDate = referralDate;
  }

  public String getUrgencyLevel() {
    return urgencyLevel;
  }

  public void setUrgencyLevel(String urgencyLevel) {
    this.urgencyLevel = urgencyLevel;
  }

  public String getReferralReason() {
    return referralReason;
  }

  public void setReferralReason(String referralReason) {
    this.referralReason = referralReason;
  }

  public String getClinicalSummary() {
    return clinicalSummary;
  }

  public void setClinicalSummary(String clinicalSummary) {
    this.clinicalSummary = clinicalSummary;
  }

  public String getRequestedInvestigations() {
    return requestedInvestigations;
  }

  public void setRequestedInvestigations(String requestedInvestigations) {
    this.requestedInvestigations = requestedInvestigations;
  }

  public ReferralStatus getStatus() {
    return status;
  }

  public void setStatus(ReferralStatus status) {
    this.status = status;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public String toString() {
    return "Referral{id='" + id
        + "', patientId='" + patientId
        + "', referringClinicianId='" + referringClinicianId
        + "', referredToClinicianId='" + referredToClinicianId
        + "', referringFacilityId='" + referringFacilityId
        + "', referredToFacilityId='" + referredToFacilityId
        + "', referralDate='" + referralDate
        + "', urgencyLevel='" + urgencyLevel
        + "', referralReason='" + referralReason
        + "', status='" + (status == null ? "" : status.name())
        + "'}";
  }
}