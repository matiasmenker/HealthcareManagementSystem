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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReferralManager {

  private static ReferralManager instance;

  private final ReferralRepository referralRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final FacilityRepository facilityRepository;
  private final ReferralProcessingOutputWriter referralProcessingOutputWriter;

  private final List<Referral> processedReferrals = new ArrayList<>();

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

  public static ReferralManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("ReferralManager has not been initialized");
    }
    return instance;
  }

  public static ReferralManager getInstance(ReferralRepository referralRepository,
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

  public void createReferral(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    referralRepository.add(referral);
    processedReferrals.add(referral);

    Patient patient = patientRepository.findById(referral.getPatientId());
    Clinician referringClinician = clinicianRepository.findById(referral.getReferringClinicianId());
    Clinician referredClinician = clinicianRepository.findById(referral.getReferredToClinicianId());
    Facility referringFacility = facilityRepository.findById(referral.getReferringFacilityId());
    Facility referredFacility = facilityRepository.findById(referral.getReferredToFacilityId());

    referralProcessingOutputWriter.writeReferralTextFile(
        referral,
        patient,
        referringClinician,
        referredClinician,
        referringFacility,
        referredFacility
    );

    referralProcessingOutputWriter.appendReferralEmailLog(
        referral,
        patient,
        referredClinician
    );

    referralProcessingOutputWriter.appendElectronicHealthRecordUpdateLog(
        referral,
        patient
    );
  }

  public List<Referral> getProcessedReferrals() {
    return new ArrayList<>(processedReferrals);
  }
}