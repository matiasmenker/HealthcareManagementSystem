package hms.controller;

import hms.model.Clinician;
import hms.repository.ClinicianRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClinicianController {

  private final ClinicianRepository clinicianRepository;

  public ClinicianController(ClinicianRepository clinicianRepository) {
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
  }

  public List<Clinician> getAllClinicians() {
    return new ArrayList<>(clinicianRepository.findAll());
  }

  public Clinician getClinicianById(String clinicianId) {
    return clinicianRepository.findById(clinicianId);
  }

  public void addClinician(Clinician clinician) {
    validateClinicianForCreateOrUpdate(clinician);
    clinicianRepository.add(clinician);
  }

  public void updateClinician(Clinician clinician) {
    validateClinicianForCreateOrUpdate(clinician);
    clinicianRepository.update(clinician);
  }

  public int getCliniciansCount() {
    return clinicianRepository.findAll().size();
  }

  private void validateClinicianForCreateOrUpdate(Clinician clinician) {
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician is required");
    }
    if (isBlank(clinician.getId())) {
      throw new IllegalArgumentException("Clinician ID is required");
    }
    if (isBlank(clinician.getFullName())) {
      throw new IllegalArgumentException("Clinician full name is required");
    }
    if (isBlank(clinician.getEmail())) {
      throw new IllegalArgumentException("Clinician email is required");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}