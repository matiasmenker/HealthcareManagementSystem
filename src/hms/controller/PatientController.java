package hms.controller;

import hms.model.Patient;
import hms.repository.PatientRepository;

import java.util.List;
import java.util.Objects;

public class PatientController {

	private final PatientRepository patientRepository;

	public PatientController(PatientRepository patientRepository) {
		this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
	}

	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	public Patient getPatientById(String patientId) {
		if (patientId == null || patientId.trim().isEmpty()) {
			throw new IllegalArgumentException("Patient id is required");
		}

		Patient patient = patientRepository.findById(patientId);
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found: " + patientId);
		}

		return patient;
	}

	public void addPatient(Patient patient) {
		validatePatient(patient);
		patientRepository.add(patient);
	}

	public void updatePatient(Patient patient) {
		validatePatient(patient);
		patientRepository.update(patient);
	}

	public void deletePatientById(String patientId) {
		if (patientId == null || patientId.trim().isEmpty()) {
			throw new IllegalArgumentException("Patient id is required");
		}
		patientRepository.deleteById(patientId.trim());
	}

	private void validatePatient(Patient patient) {
		Objects.requireNonNull(patient, "patient");

		if (patient.getId() == null || patient.getId().trim().isEmpty()) {
			throw new IllegalArgumentException("Patient id is required");
		}
		if (patient.getFullName() == null || patient.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Patient full name is required");
		}
	}
}