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

  private final ReferralRepository referralRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final FacilityRepository facilityRepository;

  public ReferralController(ReferralRepository referralRepository,
                            PatientRepository patientRepository,
                            ClinicianRepository clinicianRepository,
                            FacilityRepository facilityRepository) {
    this.referralRepository = Objects.requireNonNull(referralRepository, "referralRepository");
    this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
    this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
  }

  public void loadReferralsFromCsv(String filePath) {
    referralRepository.load(filePath);
  }

  public List<Referral> getAllReferrals() {
    return referralRepository.findAll();
  }

  public void createReferral(Referral referral) {
    validateReferralReferences(referral);
    ReferralManager.getInstance().createReferral(referral);
  }

  public void updateReferral(Referral referral) {
    validateReferralReferences(referral);
    referralRepository.update(referral);
  }

  private void validateReferralReferences(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    if (isBlank(referral.getId())) {
      throw new IllegalArgumentException("Referral id is required");
    }
    if (isBlank(referral.getPatientId())) {
      throw new IllegalArgumentException("Patient id is required");
    }
    if (isBlank(referral.getReferringClinicianId())) {
      throw new IllegalArgumentException("Referring clinician id is required");
    }
    if (isBlank(referral.getReferredToClinicianId())) {
      throw new IllegalArgumentException("Referred to clinician id is required");
    }
    if (isBlank(referral.getReferringFacilityId())) {
      throw new IllegalArgumentException("Referring facility id is required");
    }
    if (isBlank(referral.getReferredToFacilityId())) {
      throw new IllegalArgumentException("Referred to facility id is required");
    }
    if (referral.getStatus() == null) {
      throw new IllegalArgumentException("Referral status is required");
    }

    Patient patient = patientRepository.findById(referral.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + referral.getPatientId());
    }

    Clinician referringClinician = clinicianRepository.findById(referral.getReferringClinicianId());
    if (referringClinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + referral.getReferringClinicianId());
    }

    Clinician referredToClinician = clinicianRepository.findById(referral.getReferredToClinicianId());
    if (referredToClinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + referral.getReferredToClinicianId());
    }

    Facility referringFacility = facilityRepository.findById(referral.getReferringFacilityId());
    if (referringFacility == null) {
      throw new IllegalArgumentException("Facility not found: " + referral.getReferringFacilityId());
    }

    Facility referredToFacility = facilityRepository.findById(referral.getReferredToFacilityId());
    if (referredToFacility == null) {
      throw new IllegalArgumentException("Facility not found: " + referral.getReferredToFacilityId());
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}