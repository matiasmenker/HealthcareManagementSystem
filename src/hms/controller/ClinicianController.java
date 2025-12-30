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

  public List<Clinician> getAllClinicians() {
    return clinicianRepository.findAll();
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

    if (isBlank(clinician.getId())) {
      throw new IllegalArgumentException("Clinician id is required");
    }
    if (isBlank(clinician.getFirstName())) {
      throw new IllegalArgumentException("First name is required");
    }
    if (isBlank(clinician.getLastName())) {
      throw new IllegalArgumentException("Last name is required");
    }
    if (isBlank(clinician.getTitle())) {
      throw new IllegalArgumentException("Title is required");
    }
    if (isBlank(clinician.getSpeciality())) {
      throw new IllegalArgumentException("Speciality is required");
    }
    if (isBlank(clinician.getGmcNumber())) {
      throw new IllegalArgumentException("GMC number is required");
    }
    if (isBlank(clinician.getPhoneNumber())) {
      throw new IllegalArgumentException("Phone number is required");
    }
    if (isBlank(clinician.getEmail())) {
      throw new IllegalArgumentException("Email is required");
    }
    if (isBlank(clinician.getWorkplaceId())) {
      throw new IllegalArgumentException("Workplace is required");
    }
    if (isBlank(clinician.getEmploymentStatus())) {
      throw new IllegalArgumentException("Employment status is required");
    }
    if (isBlank(clinician.getStartDate())) {
      throw new IllegalArgumentException("Start date is required");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}