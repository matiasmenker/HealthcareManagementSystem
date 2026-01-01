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

	private final List<Referral> processedReferrals;

	private ReferralManager(ReferralRepository referralRepository, PatientRepository patientRepository,
			ClinicianRepository clinicianRepository, FacilityRepository facilityRepository,
			ReferralProcessingOutputWriter referralProcessingOutputWriter) {
		this.referralRepository = Objects.requireNonNull(referralRepository, "referralRepository");
		this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
		this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
		this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
		this.referralProcessingOutputWriter = Objects.requireNonNull(referralProcessingOutputWriter,
				"referralProcessingOutputWriter");
		this.processedReferrals = new ArrayList<>();
	}

	public static synchronized ReferralManager getInstance(ReferralRepository referralRepository,
			PatientRepository patientRepository, ClinicianRepository clinicianRepository,
			FacilityRepository facilityRepository, ReferralProcessingOutputWriter referralProcessingOutputWriter) {
		if (instance == null) {
			instance = new ReferralManager(referralRepository, patientRepository, clinicianRepository,
					facilityRepository, referralProcessingOutputWriter);
		}
		return instance;
	}

	public synchronized void createReferral(Referral referral) {
		Objects.requireNonNull(referral, "referral");

		Patient patient = patientRepository.findById(referral.getPatientId());
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found: " + referral.getPatientId());
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

		referralRepository.add(referral);
		processedReferrals.add(referral);

		referralProcessingOutputWriter.writeReferralDetailsFile(referral);
		referralProcessingOutputWriter.appendReferralEmailLog(referral, patient, referredClinician);
		referralProcessingOutputWriter.appendElectronicHealthRecordUpdateLog(referral, patient);
	}

	public synchronized void updateReferral(Referral referral) {
		Objects.requireNonNull(referral, "referral");

		Patient patient = patientRepository.findById(referral.getPatientId());
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found: " + referral.getPatientId());
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

		referralRepository.update(referral);

		referralProcessingOutputWriter.writeReferralDetailsFile(referral);
		referralProcessingOutputWriter.appendReferralEmailLog(referral, patient, referredClinician);
		referralProcessingOutputWriter.appendElectronicHealthRecordUpdateLog(referral, patient);
	}

	public List<Referral> getProcessedReferrals() {
		return new ArrayList<>(processedReferrals);
	}
}