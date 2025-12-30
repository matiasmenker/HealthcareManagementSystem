package hms.controller;

import hms.model.Clinician;
import hms.model.Patient;
import hms.model.Prescription;
import hms.output.PrescriptionOutputFileGenerator;
import hms.repository.ClinicianRepository;
import hms.repository.PatientRepository;
import hms.repository.PrescriptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrescriptionController {

  private final PrescriptionRepository prescriptionRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final PrescriptionOutputFileGenerator prescriptionOutputFileGenerator;

  public PrescriptionController(PrescriptionRepository prescriptionRepository,
                                PatientRepository patientRepository,
                                ClinicianRepository clinicianRepository,
                                PrescriptionOutputFileGenerator prescriptionOutputFileGenerator) {
    this.prescriptionRepository = Objects.requireNonNull(prescriptionRepository, "prescriptionRepository");
    this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
    this.prescriptionOutputFileGenerator = Objects.requireNonNull(prescriptionOutputFileGenerator, "prescriptionOutputFileGenerator");
  }

  public List<Prescription> getAllPrescriptions() {
    return new ArrayList<>(prescriptionRepository.findAll());
  }

  public void addPrescription(Prescription prescription) {
    addPrescriptionAndGenerateOutput(prescription);
  }

  public String addPrescriptionAndGenerateOutput(Prescription prescription) {
    validatePrescriptionForCreateOrUpdate(prescription);

    Patient patient = findPatientById(prescription.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + safe(prescription.getPatientId()));
    }

    Clinician clinician = clinicianRepository.findById(prescription.getClinicianId());
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + safe(prescription.getClinicianId()));
    }

    prescriptionRepository.add(prescription);

    return prescriptionOutputFileGenerator.generatePrescriptionTextFile(prescription, patient, clinician);
  }

  public void updatePrescription(Prescription prescription) {
    validatePrescriptionForCreateOrUpdate(prescription);

    Patient patient = findPatientById(prescription.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + safe(prescription.getPatientId()));
    }

    Clinician clinician = clinicianRepository.findById(prescription.getClinicianId());
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + safe(prescription.getClinicianId()));
    }

    prescriptionRepository.update(prescription);
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

  private void validatePrescriptionForCreateOrUpdate(Prescription prescription) {
    if (prescription == null) {
      throw new IllegalArgumentException("Prescription is required");
    }
    if (isBlank(prescription.getId())) {
      throw new IllegalArgumentException("Prescription ID is required");
    }
    if (isBlank(prescription.getPatientId())) {
      throw new IllegalArgumentException("Patient is required");
    }
    if (isBlank(prescription.getClinicianId())) {
      throw new IllegalArgumentException("Clinician is required");
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
      throw new IllegalArgumentException("Date Issued is required");
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