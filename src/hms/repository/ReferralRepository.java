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
    if (referralId == null) {
      return null;
    }
    return referralsById.get(referralId.trim());
  }

  public void add(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    String referralId = safe(referral.getId());
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

    String referralId = safe(referral.getId());
    if (referralId.isEmpty()) {
      throw new IllegalArgumentException("Referral id is required");
    }

    Referral existing = referralsById.get(referralId);
    if (existing == null) {
      throw new IllegalArgumentException("Referral not found: " + referralId);
    }

    existing.setPatientId(referral.getPatientId());
    existing.setFromClinicianId(referral.getFromClinicianId());
    existing.setToClinicianId(referral.getToClinicianId());
    existing.setFromFacilityId(referral.getFromFacilityId());
    existing.setToFacilityId(referral.getToFacilityId());
    existing.setUrgency(referral.getUrgency());
    existing.setClinicalSummary(referral.getClinicalSummary());
    existing.setStatus(referral.getStatus());
    existing.setDateCreated(referral.getDateCreated());
  }

  private Referral mapRowToReferral(Map<String, String> row) {
    String referralId = value(row, "referral_id");
    String patientId = value(row, "patient_id");
    String fromClinicianId = value(row, "referring_clinician_id");
    String toClinicianId = value(row, "referred_to_clinician_id");
    String fromFacilityId = value(row, "referring_facility_id");
    String toFacilityId = value(row, "referred_to_facility_id");

    String urgencyLevel = value(row, "urgency_level");
    String clinicalSummary = value(row, "clinical_summary");
    String rawStatus = value(row, "status");
    String createdDate = value(row, "created_date");

    if (safe(referralId).isEmpty()) {
      throw new IllegalArgumentException("Referral row is missing required field: referral_id");
    }

    String normalizedStatus = parseReferralStatus(rawStatus).name();

    return new Referral(
        referralId,
        patientId,
        fromClinicianId,
        toClinicianId,
        fromFacilityId,
        toFacilityId,
        urgencyLevel,
        clinicalSummary,
        normalizedStatus,
        createdDate
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

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}