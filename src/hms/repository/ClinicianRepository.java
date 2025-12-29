package hms.repository;

import hms.model.Clinician;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClinicianRepository extends BaseRepository {

	private final List<Clinician> clinicians = new ArrayList<>();
	private final Map<String, Clinician> cliniciansById = new LinkedHashMap<>();

	public void load(String filePath) {
		Objects.requireNonNull(filePath, "filePath");

		clinicians.clear();
		cliniciansById.clear();

		List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
		for (Map<String, String> row : rows) {
			Clinician clinician = mapRowToClinician(row);
			add(clinician);
		}
	}

	public List<Clinician> findAll() {
		return new ArrayList<>(clinicians);
	}

	public Clinician findById(String clinicianId) {
		if (clinicianId == null) {
			return null;
		}
		return cliniciansById.get(clinicianId);
	}

	public void add(Clinician clinician) {
		Objects.requireNonNull(clinician, "clinician");

		String clinicianId = clinician.getId();
		if (clinicianId == null || clinicianId.trim().isEmpty()) {
			throw new IllegalArgumentException("Clinician id is required");
		}
		if (cliniciansById.containsKey(clinicianId)) {
			throw new IllegalArgumentException("Duplicate clinician id: " + clinicianId);
		}

		clinicians.add(clinician);
		cliniciansById.put(clinicianId, clinician);
	}

	public void update(Clinician clinician) {
		Objects.requireNonNull(clinician, "clinician");

		String clinicianId = clinician.getId();
		if (clinicianId == null || clinicianId.trim().isEmpty()) {
			throw new IllegalArgumentException("Clinician id is required");
		}

		Clinician existing = cliniciansById.get(clinicianId);
		if (existing == null) {
			throw new IllegalArgumentException("Clinician not found: " + clinicianId);
		}

		existing.setFullName(clinician.getFullName());
		existing.setEmail(clinician.getEmail());
		existing.setTitle(clinician.getTitle());
		existing.setSpecialty(clinician.getSpecialty());
		existing.setMedicalRegistrationNumber(clinician.getMedicalRegistrationNumber());
		existing.setPhoneNumber(clinician.getPhoneNumber());
		existing.setWorkplaceId(clinician.getWorkplaceId());
		existing.setWorkplaceType(clinician.getWorkplaceType());
		existing.setEmploymentStatus(clinician.getEmploymentStatus());
		existing.setStartDate(clinician.getStartDate());
	}

	private Clinician mapRowToClinician(Map<String, String> row) {
		String clinicianId = value(row, "clinician_id");
		String firstName = value(row, "first_name");
		String lastName = value(row, "last_name");
		String title = value(row, "title");
		String specialty = value(row, "speciality");
		String medicalRegistrationNumber = value(row, "gmc_number");
		String phoneNumber = value(row, "phone_number");
		String email = value(row, "email");
		String workplaceId = value(row, "workplace_id");
		String workplaceType = value(row, "workplace_type");
		String employmentStatus = value(row, "employment_status");
		String startDate = value(row, "start_date");

		if (clinicianId.isEmpty()) {
			throw new IllegalArgumentException("Clinician row is missing required field: clinician_id");
		}

		String fullName = (firstName + " " + lastName).trim();

		return new Clinician(clinicianId, fullName, email, title, specialty, medicalRegistrationNumber, phoneNumber,
				workplaceId, workplaceType, employmentStatus, startDate);
	}
}