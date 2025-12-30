package hms.model.singleton;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;
import hms.output.ReferralProcessingOutputWriter;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;
import hms.repository.ReferralRepository;

import java.util.List;
import java.util.Objects;

public class ReferralManager {

  private static ReferralManager instance;

  private final ReferralRepository referralRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final FacilityRepository facilityRepository;
  private final ReferralProcessingOutputWriter referralProcessingOutputWriter;

  private ReferralManager(ReferralRepository referralRepository,
                          PatientRepository patientRepository,
                          ClinicianRepository clinicianRepository,
                          FacilityRepository facilityRepository,
                          ReferralProcessingOutputWriter referralProcessingOutputWriter) {
    this.referralRepository = Objects.requireNonNull(referralRepository, "referralRepository");
    this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
    this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
    this.referralProcessingOutputWriter = Objects.requireNonNull(referralProcessingOutputWriter, "referralProcessingOutputWriter");
  }

  public static synchronized ReferralManager getInstance(ReferralRepository referralRepository,
                                                        PatientRepository patientRepository,
                                                        ClinicianRepository clinicianRepository,
                                                        FacilityRepository facilityRepository,
                                                        ReferralProcessingOutputWriter referralProcessingOutputWriter) {
    if (instance == null) {
      instance = new ReferralManager(
          referralRepository,
          patientRepository,
          clinicianRepository,
          facilityRepository,
          referralProcessingOutputWriter
      );
    }
    return instance;
  }

  public static synchronized ReferralManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("ReferralManager is not initialized. Initialize it in Main before using it.");
    }
    return instance;
  }

  public ReferralProcessingResult createReferral(Referral referral) {
    if (referral == null) {
      throw new IllegalArgumentException("Referral is required");
    }

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

    referralRepository.add(referral);

    String referralTextFilePath = referralProcessingOutputWriter.writeReferralTextFile(
        referral,
        patient,
        fromClinician,
        toClinician,
        fromFacility,
        toFacility
    );

    String referralEmailsLogPath = referralProcessingOutputWriter.appendReferralEmailLog(
        referral,
        patient,
        fromClinician,
        toClinician
    );

    String electronicHealthRecordUpdatesLogPath = referralProcessingOutputWriter.appendElectronicHealthRecordUpdatesLog(
        referral,
        patient
    );

    return new ReferralProcessingResult(referralTextFilePath, referralEmailsLogPath, electronicHealthRecordUpdatesLogPath);
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

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}