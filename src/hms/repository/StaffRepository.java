package hms.repository;

import hms.model.Staff;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StaffRepository {

  private final List<Staff> staffMembers;

  public StaffRepository() {
    this.staffMembers = new ArrayList<>();
  }

  public void load(String filePath) {
    staffMembers.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      String staffId = read(row, "staff_id");
      String firstName = read(row, "first_name");
      String lastName = read(row, "last_name");
      String email = read(row, "email");

      String role = read(row, "role");
      String facilityId = read(row, "facility_id");

      String fullName = buildFullName(firstName, lastName);

      Staff staff = new Staff(
          staffId,
          fullName,
          email,
          role,
          facilityId
      );

      staffMembers.add(staff);
    }
  }

  public List<Staff> findAll() {
    return new ArrayList<>(staffMembers);
  }

  public Staff findById(String id) {
    String normalizedId = normalize(id);
    if (normalizedId.isEmpty()) {
      return null;
    }

    for (Staff staff : staffMembers) {
      if (staff == null) {
        continue;
      }
      if (normalizedId.equals(normalize(staff.getId()))) {
        return staff;
      }
    }
    return null;
  }

  public void add(Staff staff) {
    if (staff == null) {
      throw new IllegalArgumentException("Staff is required");
    }
    String staffId = normalize(staff.getId());
    if (staffId.isEmpty()) {
      throw new IllegalArgumentException("Staff id is required");
    }
    if (findById(staffId) != null) {
      throw new IllegalArgumentException("Staff id already exists: " + staffId);
    }
    staffMembers.add(staff);
  }

  public void update(Staff staff) {
    if (staff == null) {
      throw new IllegalArgumentException("Staff is required");
    }
    String staffId = normalize(staff.getId());
    if (staffId.isEmpty()) {
      throw new IllegalArgumentException("Staff id is required");
    }

    for (int index = 0; index < staffMembers.size(); index++) {
      Staff existingStaff = staffMembers.get(index);
      if (existingStaff == null) {
        continue;
      }
      if (staffId.equals(normalize(existingStaff.getId()))) {
        staffMembers.set(index, staff);
        return;
      }
    }

    throw new IllegalArgumentException("Staff not found: " + staffId);
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