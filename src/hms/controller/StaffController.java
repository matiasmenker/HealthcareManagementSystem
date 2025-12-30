package hms.controller;

import hms.model.Staff;
import hms.repository.StaffRepository;

import java.util.List;
import java.util.Objects;

public class StaffController {

  private final StaffRepository staffRepository;

  public StaffController(StaffRepository staffRepository) {
    this.staffRepository = Objects.requireNonNull(staffRepository, "staffRepository");
  }

  public void loadStaffFromCsv(String filePath) {
    staffRepository.load(filePath);
  }

  public List<Staff> getAllStaff() {
    return staffRepository.findAll();
  }

  public void addStaff(Staff staff) {
    validateStaff(staff);
    staffRepository.add(staff);
  }

  public void updateStaff(Staff staff) {
    validateStaff(staff);
    staffRepository.update(staff);
  }

  private void validateStaff(Staff staff) {
    Objects.requireNonNull(staff, "staff");

    if (isBlank(staff.getId())) {
      throw new IllegalArgumentException("Staff id is required");
    }
    if (isBlank(staff.getFullName())) {
      throw new IllegalArgumentException("Full name is required");
    }
    if (isBlank(staff.getEmail())) {
      throw new IllegalArgumentException("Email is required");
    }
    if (isBlank(staff.getRole())) {
      throw new IllegalArgumentException("Role is required");
    }
    if (isBlank(staff.getFacilityId())) {
      throw new IllegalArgumentException("Facility is required");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}