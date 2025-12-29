package hms.repository;

import hms.model.Patient;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PatientRepository extends BaseRepository {
  private final List<Patient> patients = new ArrayList<>();
  private final Map<String, Patient> patientsById = new LinkedHashMap<>();

  public void load(String filePath) {
    Objects.requireNonNull(filePath, "filePath");

    patients.clear();
    patientsById.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      Patient patient = mapRowToPatient(row);
      add(patient);
    }
  }

  public List<Patient> findAll() {
    return new ArrayList<>(patients);
  }

  public Patient findById(String id) {
    if (id == null) {
      return null;
    }
    return patientsById.get(id);
  }

  public void add(Patient patient) {
    Objects.requireNonNull(patient, "patient");

    String id = patient.getId();
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Patient id is required");
    }
    if (patientsById.containsKey(id)) {
      throw new IllegalArgumentException("Duplicate patient id: " + id);
    }

    patients.add(patient);
    patientsById.put(id, patient);
  }

  public void update(Patient patient) {
    Objects.requireNonNull(patient, "patient");

    String id = patient.getId();
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Patient id is required");
    }

    Patient existing = patientsById.get(id);
    if (existing == null) {
      throw new IllegalArgumentException("Patient not found: " + id);
    }

    existing.setFullName(patient.getFullName());
    existing.setEmail(patient.getEmail());
    existing.setNhsNumber(patient.getNhsNumber());
    existing.setPhone(patient.getPhone());
    existing.setAddress(patient.getAddress());
    existing.setRegisteredFacilityId(patient.getRegisteredFacilityId());
  }

  public void deleteById(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Patient id is required");
    }

    Patient existing = patientsById.remove(id);
    if (existing == null) {
      throw new IllegalArgumentException("Patient not found: " + id);
    }

    patients.remove(existing);
  }

  private Patient mapRowToPatient(Map<String, String> row) {
    String id = value(row, "patient_id");
    String firstName = value(row, "first_name");
    String lastName = value(row, "last_name");
    String email = value(row, "email");
    String nhsNumber = value(row, "nhs_number");
    String phoneNumber = value(row, "phone_number");
    String address = value(row, "address");
    String gpSurgeryId = value(row, "gp_surgery_id");

    if (id.isEmpty()) {
      throw new IllegalArgumentException("Patient row is missing required field: patient_id");
    }
    if (firstName.isEmpty() && lastName.isEmpty()) {
      throw new IllegalArgumentException("Patient row is missing required fields: first_name/last_name (patient_id=" + id + ")");
    }

    String fullName = (firstName + " " + lastName).trim();

    return new Patient(
        id,
        fullName,
        email,
        nhsNumber,
        phoneNumber,
        address,
        gpSurgeryId
    );
  }
}