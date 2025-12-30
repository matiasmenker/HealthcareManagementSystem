package hms.repository;

import hms.model.Clinician;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClinicianRepository {

  private final List<Clinician> clinicians;

  public ClinicianRepository() {
    this.clinicians = new ArrayList<>();
  }

  public void load(String filePath) {
    clinicians.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      String clinicianId = read(row, "clinician_id");
      String firstName = read(row, "first_name");
      String lastName = read(row, "last_name");
      String email = read(row, "email");

      String title = read(row, "title");
      String speciality = read(row, "speciality");
      String workplaceId = read(row, "workplace_id");

      String fullName = buildFullName(firstName, lastName);

      Clinician clinician = new Clinician(
          clinicianId,
          fullName,
          email,
          title,
          speciality,
          workplaceId
      );

      clinicians.add(clinician);
    }
  }

  public List<Clinician> findAll() {
    return new ArrayList<>(clinicians);
  }

  public Clinician findById(String id) {
    String normalizedId = normalize(id);
    if (normalizedId.isEmpty()) {
      return null;
    }

    for (Clinician clinician : clinicians) {
      if (clinician == null) {
        continue;
      }
      if (normalizedId.equals(normalize(clinician.getId()))) {
        return clinician;
      }
    }
    return null;
  }

  public void add(Clinician clinician) {
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician is required");
    }
    String clinicianId = normalize(clinician.getId());
    if (clinicianId.isEmpty()) {
      throw new IllegalArgumentException("Clinician id is required");
    }
    if (findById(clinicianId) != null) {
      throw new IllegalArgumentException("Clinician id already exists: " + clinicianId);
    }
    clinicians.add(clinician);
  }

  public void update(Clinician clinician) {
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician is required");
    }
    String clinicianId = normalize(clinician.getId());
    if (clinicianId.isEmpty()) {
      throw new IllegalArgumentException("Clinician id is required");
    }

    for (int index = 0; index < clinicians.size(); index++) {
      Clinician existingClinician = clinicians.get(index);
      if (existingClinician == null) {
        continue;
      }
      if (clinicianId.equals(normalize(existingClinician.getId()))) {
        clinicians.set(index, clinician);
        return;
      }
    }

    throw new IllegalArgumentException("Clinician not found: " + clinicianId);
  }

  private String read(Map<String, String> row, String key) {
    if (row == null) {
      return "";
    }
    String value = row.getOrDefault(key, "");
    if (value == null) {
      return "";
    }
    return value.trim();
  }

  private String buildFullName(String firstName, String lastName) {
    String left = normalize(firstName);
    String right = normalize(lastName);

    if (!left.isEmpty() && !right.isEmpty()) {
      return left + " " + right;
    }
    if (!left.isEmpty()) {
      return left;
    }
    if (!right.isEmpty()) {
      return right;
    }
    return "";
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}