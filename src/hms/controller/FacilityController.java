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

  public List<Facility> getAllFacilities() {
    return facilityRepository.findAll();
  }

  public Facility getFacilityById(String facilityId) {
    return facilityRepository.findById(facilityId);
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

    if (isBlank(facility.getId())) {
      throw new IllegalArgumentException("Facility id is required");
    }
    if (isBlank(facility.getName())) {
      throw new IllegalArgumentException("Facility name is required");
    }
    if (isBlank(facility.getType())) {
      throw new IllegalArgumentException("Facility type is required");
    }
    if (isBlank(facility.getAddress())) {
      throw new IllegalArgumentException("Facility address is required");
    }
    if (isBlank(facility.getPostcode())) {
      throw new IllegalArgumentException("Facility postcode is required");
    }
    if (isBlank(facility.getPhoneNumber())) {
      throw new IllegalArgumentException("Facility phone number is required");
    }
    if (isBlank(facility.getEmail())) {
      throw new IllegalArgumentException("Facility email is required");
    }
    if (isBlank(facility.getOpeningHours())) {
      throw new IllegalArgumentException("Facility opening hours are required");
    }
    if (isBlank(facility.getManagerName())) {
      throw new IllegalArgumentException("Facility manager name is required");
    }
    if (isBlank(facility.getCapacity())) {
      throw new IllegalArgumentException("Facility capacity is required");
    }
    if (isBlank(facility.getSpecialitiesOffered())) {
      throw new IllegalArgumentException("Facility specialities offered are required");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}