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

	public Staff getStaffById(String staffId) {
		if (staffId == null || staffId.trim().isEmpty()) {
			throw new IllegalArgumentException("Staff id is required");
		}

		Staff staff = staffRepository.findById(staffId);
		if (staff == null) {
			throw new IllegalArgumentException("Staff not found: " + staffId);
		}

		return staff;
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

		if (staff.getId() == null || staff.getId().trim().isEmpty()) {
			throw new IllegalArgumentException("Staff id is required");
		}
		if (staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Staff full name is required");
		}
		if (staff.getRole() == null || staff.getRole().trim().isEmpty()) {
			throw new IllegalArgumentException("Staff role is required");
		}
	}
}