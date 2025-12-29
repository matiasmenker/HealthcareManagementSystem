package hms.repository;

import hms.model.Staff;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StaffRepository extends BaseRepository {

  private final List<Staff> staffMembers = new ArrayList<>();
  private final Map<String, Staff> staffById = new LinkedHashMap<>();

  public void load(String filePath) {
    Objects.requireNonNull(filePath, "filePath");

    staffMembers.clear();
    staffById.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      Staff staff = mapRowToStaff(row);
      add(staff);
    }
  }

  public List<Staff> findAll() {
    return new ArrayList<>(staffMembers);
  }

  public Staff findById(String staffId) {
    if (staffId == null) {
      return null;
    }
    return staffById.get(staffId);
  }

  public void add(Staff staff) {
    Objects.requireNonNull(staff, "staff");

    String staffId = staff.getId();
    if (staffId == null || staffId.trim().isEmpty()) {
      throw new IllegalArgumentException("Staff id is required");
    }
    if (staffById.containsKey(staffId)) {
      throw new IllegalArgumentException("Duplicate staff id: " + staffId);
    }

    staffMembers.add(staff);
    staffById.put(staffId, staff);
  }

  public void update(Staff staff) {
    Objects.requireNonNull(staff, "staff");

    String staffId = staff.getId();
    if (staffId == null || staffId.trim().isEmpty()) {
      throw new IllegalArgumentException("Staff id is required");
    }

    Staff existing = staffById.get(staffId);
    if (existing == null) {
      throw new IllegalArgumentException("Staff not found: " + staffId);
    }

    existing.setFullName(staff.getFullName());
    existing.setEmail(staff.getEmail());
    existing.setRole(staff.getRole());
    existing.setDepartment(staff.getDepartment());
    existing.setPhoneNumber(staff.getPhoneNumber());
    existing.setWorkLocation(staff.getWorkLocation());
    existing.setShiftPattern(staff.getShiftPattern());
    existing.setEmploymentStatus(staff.getEmploymentStatus());
    existing.setStartDate(staff.getStartDate());
    existing.setSupervisorName(staff.getSupervisorName());
    existing.setAccessLevel(staff.getAccessLevel());
  }

  private Staff mapRowToStaff(Map<String, String> row) {
    String staffId = value(row, "staff_id");
    String firstName = value(row, "first_name");
    String lastName = value(row, "last_name");
    String role = value(row, "role");
    String department = value(row, "department");
    String phoneNumber = value(row, "phone_number");
    String email = value(row, "email");
    String workLocation = value(row, "work_location");
    String shiftPattern = value(row, "shift_pattern");
    String employmentStatus = value(row, "employment_status");
    String startDate = value(row, "start_date");
    String supervisorName = value(row, "supervisor_name");
    String accessLevel = value(row, "access_level");

    if (staffId.isEmpty()) {
      throw new IllegalArgumentException("Staff row is missing required field: staff_id");
    }

    String fullName = (firstName + " " + lastName).trim();

    return new Staff(
        staffId,
        fullName,
        email,
        role,
        department,
        phoneNumber,
        workLocation,
        shiftPattern,
        employmentStatus,
        startDate,
        supervisorName,
        accessLevel
    );
  }
}