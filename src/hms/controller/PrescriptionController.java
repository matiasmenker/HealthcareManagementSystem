package hms.controller;

import hms.model.Clinician;
import hms.model.Patient;
import hms.model.Prescription;
import hms.output.PrescriptionOutputFileGenerator;
import hms.repository.ClinicianRepository;
import hms.repository.PatientRepository;
import hms.repository.PrescriptionRepository;

import java.util.List;
import java.util.Objects;

public class PrescriptionController {

	private final PrescriptionRepository prescriptionRepository;
	private final PatientRepository patientRepository;
	private final ClinicianRepository clinicianRepository;
	private final PrescriptionOutputFileGenerator prescriptionOutputFileGenerator;

	public PrescriptionController(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository,
			ClinicianRepository clinicianRepository, PrescriptionOutputFileGenerator prescriptionOutputFileGenerator) {
		this.prescriptionRepository = Objects.requireNonNull(prescriptionRepository, "prescriptionRepository");
		this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
		this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
		this.prescriptionOutputFileGenerator = Objects.requireNonNull(prescriptionOutputFileGenerator,
				"prescriptionOutputFileGenerator");
	}

	public List<Prescription> getAllPrescriptions() {
		return prescriptionRepository.findAll();
	}

	public void addPrescription(Prescription prescription) {
		validatePrescription(prescription);
		prescriptionRepository.add(prescription);

		Patient patient = patientRepository.findById(prescription.getPatientId());
		Clinician clinician = clinicianRepository.findById(prescription.getClinicianId());

		if (patient != null && clinician != null) {
			prescriptionOutputFileGenerator.generatePrescriptionTextFile(prescription, patient, clinician);
		}
	}

	public void updatePrescription(Prescription prescription) {
		validatePrescription(prescription);
		prescriptionRepository.update(prescription);
	}

	public void deletePrescription(String prescriptionId) {
		if (isBlank(prescriptionId)) {
			throw new IllegalArgumentException("Prescription id is required");
		}
		prescriptionRepository.delete(prescriptionId);
	}

	private void validatePrescription(Prescription prescription) {
		Objects.requireNonNull(prescription, "prescription");

		if (isBlank(prescription.getId())) {
			throw new IllegalArgumentException("Prescription id is required");
		}
		if (isBlank(prescription.getPatientId())) {
			throw new IllegalArgumentException("Patient id is required");
		}
		if (isBlank(prescription.getClinicianId())) {
			throw new IllegalArgumentException("Clinician id is required");
		}
		if (isBlank(prescription.getMedication())) {
			throw new IllegalArgumentException("Medication is required");
		}
		if (isBlank(prescription.getDosage())) {
			throw new IllegalArgumentException("Dosage is required");
		}
		if (isBlank(prescription.getPharmacy())) {
			throw new IllegalArgumentException("Pharmacy is required");
		}
		if (isBlank(prescription.getCollectionStatus())) {
			throw new IllegalArgumentException("Status is required");
		}
		if (isBlank(prescription.getDateIssued())) {
			throw new IllegalArgumentException("Date issued is required");
		}

		Patient patient = patientRepository.findById(prescription.getPatientId());
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found: " + prescription.getPatientId());
		}

		Clinician clinician = clinicianRepository.findById(prescription.getClinicianId());
		if (clinician == null) {
			throw new IllegalArgumentException("Clinician not found: " + prescription.getClinicianId());
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}