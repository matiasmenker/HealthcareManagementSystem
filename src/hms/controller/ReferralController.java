package hms.controller;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;
import hms.model.singleton.ReferralManager;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;
import hms.repository.ReferralRepository;

import java.util.List;
import java.util.Objects;

public class ReferralController {

  private final ReferralManager referralManager;
  private final ReferralRepository referralRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final FacilityRepository facilityRepository;

  public ReferralController(ReferralManager referralManager,
                            ReferralRepository referralRepository,
                            PatientRepository patientRepository,
                            ClinicianRepository clinicianRepository,
                            FacilityRepository facilityRepository) {
    this.referralManager = Objects.requireNonNull(referralManager, "referralManager");
    this.referralRepository = Objects.requireNonNull(referralRepository, "referralRepository");
    this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
    this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
  }

  public List<Referral> getAllReferrals() {
    return referralRepository.findAll();
  }

  public void addReferral(Referral referral) {
    validateReferral(referral);
    referralManager.createReferral(referral);
  }

  public void updateReferral(Referral referral) {
    validateReferral(referral);
    referralManager.updateReferral(referral);
  }

  private void validateReferral(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    if (isBlank(referral.getId())) {
      throw new IllegalArgumentException("Referral id is required");
    }
    if (isBlank(referral.getPatientId())) {
      throw new IllegalArgumentException("Patient is required");
    }
    if (isBlank(referral.getReferringClinicianId())) {
      throw new IllegalArgumentException("Referring clinician is required");
    }
    if (isBlank(referral.getReferredToClinicianId())) {
      throw new IllegalArgumentException("Referred clinician is required");
    }
    if (isBlank(referral.getReferringFacilityId())) {
      throw new IllegalArgumentException("Referring facility is required");
    }
    if (isBlank(referral.getReferredToFacilityId())) {
      throw new IllegalArgumentException("Referred facility is required");
    }
    if (isBlank(referral.getReferralDate())) {
      throw new IllegalArgumentException("Referral date is required");
    }
    if (isBlank(referral.getUrgencyLevel())) {
      throw new IllegalArgumentException("Urgency level is required");
    }
    if (referral.getStatus() == null) {
      throw new IllegalArgumentException("Status is required");
    }
    if (isBlank(referral.getReferralReason())) {
      throw new IllegalArgumentException("Referral reason is required");
    }
    if (isBlank(referral.getClinicalSummary())) {
      throw new IllegalArgumentException("Clinical summary is required");
    }

    Patient patient = patientRepository.findById(referral.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + referral.getPatientId());
    }

    Clinician referringClinician = clinicianRepository.findById(referral.getReferringClinicianId());
    if (referringClinician == null) {
      throw new IllegalArgumentException("Referring clinician not found: " + referral.getReferringClinicianId());
    }

    Clinician referredClinician = clinicianRepository.findById(referral.getReferredToClinicianId());
    if (referredClinician == null) {
      throw new IllegalArgumentException("Referred clinician not found: " + referral.getReferredToClinicianId());
    }

    Facility referringFacility = facilityRepository.findById(referral.getReferringFacilityId());
    if (referringFacility == null) {
      throw new IllegalArgumentException("Referring facility not found: " + referral.getReferringFacilityId());
    }

    Facility referredFacility = facilityRepository.findById(referral.getReferredToFacilityId());
    if (referredFacility == null) {
      throw new IllegalArgumentException("Referred facility not found: " + referral.getReferredToFacilityId());
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}