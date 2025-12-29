package hms.controller;

import hms.model.Facility;
import hms.repository.FacilityRepository;

import java.util.List;
import java.util.Objects;

public class FacilityController {

  private final FacilityRepository facilityRepository;

  public FacilityController(FacilityRepository facilityRepository) {
    this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
  }

  public void loadFacilitiesFromCsv(String filePath) {
    facilityRepository.load(filePath);
  }

  public List<Facility> getAllFacilities() {
    return facilityRepository.findAll();
  }

  public Facility getFacilityById(String facilityId) {
    if (facilityId == null || facilityId.trim().isEmpty()) {
      throw new IllegalArgumentException("Facility id is required");
    }

    Facility facility = facilityRepository.findById(facilityId);
    if (facility == null) {
      throw new IllegalArgumentException("Facility not found: " + facilityId);
    }

    return facility;
  }

  public void addFacility(Facility facility) {
    validateFacility(facility);
    facilityRepository.add(facility);
  }

  public void updateFacility(Facility facility) {
    validateFacility(facility);
    facilityRepository.update(facility);
  }

  private void validateFacility(Facility facility) {
    Objects.requireNonNull(facility, "facility");

    if (facility.getId() == null || facility.getId().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility id is required");
    }
    if (facility.getName() == null || facility.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility name is required");
    }
    if (facility.getType() == null || facility.getType().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility type is required");
    }
    if (facility.getAddress() == null || facility.getAddress().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility address is required");
    }
    if (facility.getPhoneNumber() == null || facility.getPhoneNumber().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility phone number is required");
    }
    if (facility.getCapacity() == null || facility.getCapacity().trim().isEmpty()) {
      throw new IllegalArgumentException("Facility capacity is required");
    }
  }
}