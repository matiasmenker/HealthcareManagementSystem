package hms.controller;

import hms.model.Clinician;
import hms.repository.ClinicianRepository;

import java.util.List;
import java.util.Objects;

public class ClinicianController {

  private final ClinicianRepository clinicianRepository;

  public ClinicianController(ClinicianRepository clinicianRepository) {
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
  }

  public void loadCliniciansFromCsv(String filePath) {
    clinicianRepository.load(filePath);
  }

  public List<Clinician> getAllClinicians() {
    return clinicianRepository.findAll();
  }

  public Clinician getClinicianById(String clinicianId) {
    if (clinicianId == null || clinicianId.trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician id is required");
    }

    Clinician clinician = clinicianRepository.findById(clinicianId);
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + clinicianId);
    }

    return clinician;
  }

  public void addClinician(Clinician clinician) {
    validateClinician(clinician);
    clinicianRepository.add(clinician);
  }

  public void updateClinician(Clinician clinician) {
    validateClinician(clinician);
    clinicianRepository.update(clinician);
  }

  private void validateClinician(Clinician clinician) {
    Objects.requireNonNull(clinician, "clinician");

    if (clinician.getId() == null || clinician.getId().trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician id is required");
    }
    if (clinician.getFullName() == null || clinician.getFullName().trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician full name is required");
    }
    if (clinician.getTitle() == null || clinician.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician title is required");
    }
    if (clinician.getWorkplaceId() == null || clinician.getWorkplaceId().trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician workplace id is required");
    }
    if (clinician.getWorkplaceType() == null || clinician.getWorkplaceType().trim().isEmpty()) {
      throw new IllegalArgumentException("Clinician workplace type is required");
    }
  }
}