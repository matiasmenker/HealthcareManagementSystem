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

  private final List<Referral> referralQueue = new ArrayList<>();

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

  public static ReferralManager getInstance(ReferralRepository referralRepository,
                                            PatientRepository patientRepository,
                                            ClinicianRepository clinicianRepository,
                                            FacilityRepository facilityRepository,
                                            ReferralProcessingOutputWriter referralProcessingOutputWriter) {
    if (instance == null) {
      instance = new ReferralManager(referralRepository, patientRepository, clinicianRepository, facilityRepository, referralProcessingOutputWriter);
    }
    return instance;
  }

  public static ReferralManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("ReferralManager is not initialised");
    }
    return instance;
  }

  public void createReferral(Referral referral) {
    Objects.requireNonNull(referral, "referral");

    referralRepository.add(referral);
    referralQueue.add(referral);

    Patient patient = patientRepository.findById(referral.getPatientId());
    Clinician fromClinician = clinicianRepository.findById(referral.getReferringClinicianId());
    Clinician toClinician = clinicianRepository.findById(referral.getReferredToClinicianId());
    Facility fromFacility = facilityRepository.findById(referral.getReferringFacilityId());
    Facility toFacility = facilityRepository.findById(referral.getReferredToFacilityId());

    String patientName = patient == null ? "" : safe(patient.getFullName());
    String fromClinicianName = fromClinician == null ? "" : safe(fromClinician.getFullName());
    String toClinicianName = toClinician == null ? "" : safe(toClinician.getFullName());
    String fromFacilityName = fromFacility == null ? "" : safe(fromFacility.getName());
    String toFacilityName = toFacility == null ? "" : safe(toFacility.getName());

    referralProcessingOutputWriter.writeReferralFile(
        referral,
        patientName,
        fromClinicianName,
        toClinicianName,
        fromFacilityName,
        toFacilityName
    );

    String emailContent = buildReferralEmailContent(referral, patientName, fromClinician, toClinician, fromFacility, toFacility);
    referralProcessingOutputWriter.appendReferralEmailLog(emailContent);

    String ehrUpdateContent = buildEhrUpdateContent(referral, patientName, fromClinicianName, toClinicianName);
    referralProcessingOutputWriter.appendEhrUpdatesLog(ehrUpdateContent);
  }

  public List<Referral> getReferralQueueSnapshot() {
    return new ArrayList<>(referralQueue);
  }

  private String buildReferralEmailContent(Referral referral,
                                          String patientName,
                                          Clinician fromClinician,
                                          Clinician toClinician,
                                          Facility fromFacility,
                                          Facility toFacility) {
    String fromClinicianEmail = fromClinician == null ? "" : safe(fromClinician.getEmail());
    String toClinicianEmail = toClinician == null ? "" : safe(toClinician.getEmail());

    String fromFacilityName = fromFacility == null ? "" : safe(fromFacility.getName());
    String toFacilityName = toFacility == null ? "" : safe(toFacility.getName());

    return "Referral Email | Referral ID: " + safe(referral.getId())
        + " | To: " + toClinicianEmail
        + " | From: " + fromClinicianEmail
        + " | Patient: " + safe(patientName)
        + " | Urgency: " + safe(referral.getUrgencyLevel())
        + " | From Facility: " + fromFacilityName
        + " | To Facility: " + toFacilityName
        + " | Reason: " + safe(referral.getReferralReason());
  }

  private String buildEhrUpdateContent(Referral referral,
                                       String patientName,
                                       String fromClinicianName,
                                       String toClinicianName) {
    return "EHR Update | Referral ID: " + safe(referral.getId())
        + " | Patient: " + safe(patientName)
        + " | From Clinician: " + safe(fromClinicianName)
        + " | To Clinician: " + safe(toClinicianName)
        + " | Status: " + (referral.getStatus() == null ? "" : referral.getStatus().name());
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}