package hms.controller;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;
import hms.model.singleton.ReferralManager;
import hms.model.singleton.ReferralProcessingResult;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;
import hms.repository.ReferralRepository;

import java.util.ArrayList;
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

  public List<Referral> getAllReferrals() {
    return new ArrayList<>(referralRepository.findAll());
  }

  public ReferralProcessingResult addReferralAndProcessOutputs(Referral referral) {
    validateReferralForCreateOrUpdate(referral);

    Patient patient = findPatientById(referral.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + safe(referral.getPatientId()));
    }

    Clinician fromClinician = clinicianRepository.findById(referral.getFromClinicianId());
    if (fromClinician == null) {
      throw new IllegalArgumentException("From Clinician not found: " + safe(referral.getFromClinicianId()));
    }

    Clinician toClinician = clinicianRepository.findById(referral.getToClinicianId());
    if (toClinician == null) {
      throw new IllegalArgumentException("To Clinician not found: " + safe(referral.getToClinicianId()));
    }

    Facility fromFacility = facilityRepository.findById(referral.getFromFacilityId());
    if (fromFacility == null) {
      throw new IllegalArgumentException("From Facility not found: " + safe(referral.getFromFacilityId()));
    }

    Facility toFacility = facilityRepository.findById(referral.getToFacilityId());
    if (toFacility == null) {
      throw new IllegalArgumentException("To Facility not found: " + safe(referral.getToFacilityId()));
    }

    ReferralManager referralManager = ReferralManager.getInstance();
    return referralManager.createReferral(referral);
  }

  public void updateReferral(Referral referral) {
    validateReferralForCreateOrUpdate(referral);

    Patient patient = findPatientById(referral.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + safe(referral.getPatientId()));
    }

    Clinician fromClinician = clinicianRepository.findById(referral.getFromClinicianId());
    if (fromClinician == null) {
      throw new IllegalArgumentException("From Clinician not found: " + safe(referral.getFromClinicianId()));
    }

    Clinician toClinician = clinicianRepository.findById(referral.getToClinicianId());
    if (toClinician == null) {
      throw new IllegalArgumentException("To Clinician not found: " + safe(referral.getToClinicianId()));
    }

    Facility fromFacility = facilityRepository.findById(referral.getFromFacilityId());
    if (fromFacility == null) {
      throw new IllegalArgumentException("From Facility not found: " + safe(referral.getFromFacilityId()));
    }

    Facility toFacility = facilityRepository.findById(referral.getToFacilityId());
    if (toFacility == null) {
      throw new IllegalArgumentException("To Facility not found: " + safe(referral.getToFacilityId()));
    }

    referralRepository.update(referral);
  }

  private Patient findPatientById(String patientId) {
    String normalizedPatientId = safe(patientId);
    if (normalizedPatientId.isEmpty()) {
      return null;
    }

    List<Patient> patients = patientRepository.findAll();
    for (Patient patient : patients) {
      if (patient == null) {
        continue;
      }
      if (normalizedPatientId.equals(safe(patient.getId()))) {
        return patient;
      }
    }

    return null;
  }

  private void validateReferralForCreateOrUpdate(Referral referral) {
    if (referral == null) {
      throw new IllegalArgumentException("Referral is required");
    }
    if (isBlank(referral.getId())) {
      throw new IllegalArgumentException("Referral ID is required");
    }
    if (isBlank(referral.getPatientId())) {
      throw new IllegalArgumentException("Patient is required");
    }
    if (isBlank(referral.getFromClinicianId())) {
      throw new IllegalArgumentException("From Clinician is required");
    }
    if (isBlank(referral.getToClinicianId())) {
      throw new IllegalArgumentException("To Clinician is required");
    }
    if (isBlank(referral.getFromFacilityId())) {
      throw new IllegalArgumentException("From Facility is required");
    }
    if (isBlank(referral.getToFacilityId())) {
      throw new IllegalArgumentException("To Facility is required");
    }
    if (isBlank(referral.getUrgency())) {
      throw new IllegalArgumentException("Urgency is required");
    }
    if (isBlank(referral.getClinicalSummary())) {
      throw new IllegalArgumentException("Clinical Summary is required");
    }
    if (isBlank(referral.getStatus())) {
      throw new IllegalArgumentException("Status is required");
    }
    if (isBlank(referral.getDateCreated())) {
      throw new IllegalArgumentException("Date Created is required");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}