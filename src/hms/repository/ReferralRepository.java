package hms.repository;

import hms.model.Referral;
import hms.model.enums.ReferralStatus;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReferralRepository extends BaseRepository {

  private final List<Referral> referrals = new ArrayList<>();
  private final Map<String, Referral> referralsById = new LinkedHashMap<>();

  public void load(String filePath) {
    Objects.requireNonNull(filePath, "filePath");

    referrals.clear();
    referralsById.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      Referral referral = mapRowToReferral(row);
      add(referral);
    }
  }

  public List<Referral> findAll() {
    return new ArrayList<>(referrals);
  }

  public Referral findById(String referralId) {
    String normalizedId = normalizeId(referralId);
    if (normalizedId.isEmpty()) {
      return null;
    }
    return referralsById.get(normalizedId);
  }

  public void add(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    String referralId = normalizeId(referral.getId());
    if (referralId.isEmpty()) {
      throw new IllegalArgumentException("Referral id is required");
    }
    if (referralsById.containsKey(referralId)) {
      throw new IllegalArgumentException("Duplicate referral id: " + referralId);
    }

    referrals.add(referral);
    referralsById.put(referralId, referral);
  }

  public void update(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    String referralId = normalizeId(referral.getId());
    if (referralId.isEmpty()) {
      throw new IllegalArgumentException("Referral id is required");
    }

    Referral existing = referralsById.get(referralId);
    if (existing == null) {
      throw new IllegalArgumentException("Referral not found: " + referralId);
    }

    existing.setPatientId(referral.getPatientId());
    existing.setReferringClinicianId(referral.getReferringClinicianId());
    existing.setReferredToClinicianId(referral.getReferredToClinicianId());
    existing.setReferringFacilityId(referral.getReferringFacilityId());
    existing.setReferredToFacilityId(referral.getReferredToFacilityId());
    existing.setReferralDate(referral.getReferralDate());
    existing.setUrgencyLevel(referral.getUrgencyLevel());
    existing.setReferralReason(referral.getReferralReason());
    existing.setClinicalSummary(referral.getClinicalSummary());
    existing.setRequestedInvestigations(referral.getRequestedInvestigations());
    existing.setStatus(referral.getStatus());
    existing.setAppointmentId(referral.getAppointmentId());
    existing.setNotes(referral.getNotes());
    existing.setCreatedDate(referral.getCreatedDate());
    existing.setLastUpdated(referral.getLastUpdated());
  }

  public void delete(String referralId) {
    String normalizedId = normalizeId(referralId);
    if (normalizedId.isEmpty()) {
      throw new IllegalArgumentException("Referral id is required");
    }

    Referral existing = referralsById.get(normalizedId);
    if (existing == null) {
      throw new IllegalArgumentException("Referral not found: " + normalizedId);
    }

    referrals.remove(existing);
    referralsById.remove(normalizedId);
  }

  private Referral mapRowToReferral(Map<String, String> row) {
    String referralId = value(row, "referral_id");
    String patientId = value(row, "patient_id");
    String referringClinicianId = value(row, "referring_clinician_id");
    String referredToClinicianId = value(row, "referred_to_clinician_id");
    String referringFacilityId = value(row, "referring_facility_id");
    String referredToFacilityId = value(row, "referred_to_facility_id");
    String referralDate = value(row, "referral_date");
    String urgencyLevel = value(row, "urgency_level");
    String referralReason = value(row, "referral_reason");
    String clinicalSummary = value(row, "clinical_summary");
    String requestedInvestigations = value(row, "requested_investigations");
    String status = value(row, "status");
    String appointmentId = value(row, "appointment_id");
    String notes = value(row, "notes");
    String createdDate = value(row, "created_date");
    String lastUpdated = value(row, "last_updated");

    if (referralId.isEmpty()) {
      throw new IllegalArgumentException("Referral row is missing required field: referral_id");
    }

    return new Referral(
        referralId,
        patientId,
        referringClinicianId,
        referredToClinicianId,
        referringFacilityId,
        referredToFacilityId,
        referralDate,
        urgencyLevel,
        referralReason,
        clinicalSummary,
        requestedInvestigations,
        parseReferralStatus(status),
        appointmentId,
        notes,
        createdDate,
        lastUpdated
    );
  }

  private ReferralStatus parseReferralStatus(String rawStatus) {
    if (rawStatus == null) {
      return ReferralStatus.CREATED;
    }

    String normalized = rawStatus.trim().toUpperCase();
    if (normalized.isEmpty()) {
      return ReferralStatus.CREATED;
    }

    if (normalized.equals("PENDING")) {
      return ReferralStatus.SENT;
    }

    if (normalized.equals("IN PROGRESS") || normalized.equals("IN_PROGRESS")) {
      return ReferralStatus.ACCEPTED;
    }

    if (normalized.equals("COMPLETED")) {
      return ReferralStatus.COMPLETED;
    }

    if (normalized.equals("REJECTED")) {
      return ReferralStatus.REJECTED;
    }

    if (normalized.equals("SENT")) {
      return ReferralStatus.SENT;
    }

    if (normalized.equals("ACCEPTED")) {
      return ReferralStatus.ACCEPTED;
    }

    return ReferralStatus.CREATED;
  }

  private String normalizeId(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}