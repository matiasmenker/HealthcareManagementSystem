package hms.repository;

import hms.model.Facility;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FacilityRepository extends BaseRepository {

  private final List<Facility> facilities = new ArrayList<>();
  private final Map<String, Facility> facilitiesById = new LinkedHashMap<>();

  public void load(String filePath) {
    Objects.requireNonNull(filePath, "filePath");

    facilities.clear();
    facilitiesById.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      Facility facility = mapRowToFacility(row);
      add(facility);
    }
  }

  public List<Facility> findAll() {
    return new ArrayList<>(facilities);
  }

  public Facility findById(String facilityId) {
    if (facilityId == null) {
      return null;
    }
    return facilitiesById.get(facilityId);
  }

  public void add(Facility facility) {
    Objects.requireNonNull(facility, "facility");

    String facilityId = facility.getId();
    if (facilityId == null || facilityId.trim().isEmpty()) {
      throw new IllegalArgumentException("Facility id is required");
    }
    if (facilitiesById.containsKey(facilityId)) {
      throw new IllegalArgumentException("Duplicate facility id: " + facilityId);
    }

    facilities.add(facility);
    facilitiesById.put(facilityId, facility);
  }

  public void update(Facility facility) {
    Objects.requireNonNull(facility, "facility");

    String facilityId = facility.getId();
    if (facilityId == null || facilityId.trim().isEmpty()) {
      throw new IllegalArgumentException("Facility id is required");
    }

    Facility existing = facilitiesById.get(facilityId);
    if (existing == null) {
      throw new IllegalArgumentException("Facility not found: " + facilityId);
    }

    existing.setName(facility.getName());
    existing.setType(facility.getType());
    existing.setAddress(facility.getAddress());
    existing.setPostcode(facility.getPostcode());
    existing.setPhoneNumber(facility.getPhoneNumber());
    existing.setEmail(facility.getEmail());
    existing.setOpeningHours(facility.getOpeningHours());
    existing.setManagerName(facility.getManagerName());
    existing.setCapacity(facility.getCapacity());
    existing.setSpecialitiesOffered(facility.getSpecialitiesOffered());
  }

  private Facility mapRowToFacility(Map<String, String> row) {
    String facilityId = value(row, "facility_id");
    String name = value(row, "facility_name");
    String type = value(row, "facility_type");
    String address = value(row, "address");
    String postcode = value(row, "postcode");
    String phoneNumber = value(row, "phone_number");
    String email = value(row, "email");
    String openingHours = value(row, "opening_hours");
    String managerName = value(row, "manager_name");
    String capacity = value(row, "capacity");
    String specialitiesOffered = value(row, "specialities_offered");

    if (facilityId.isEmpty()) {
      throw new IllegalArgumentException("Facility row is missing required field: facility_id");
    }

    return new Facility(
        facilityId,
        name,
        type,
        address,
        postcode,
        phoneNumber,
        email,
        openingHours,
        managerName,
        capacity,
        specialitiesOffered
    );
  }
}