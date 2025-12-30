package hms.repository;

import hms.model.Prescription;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PrescriptionRepository extends BaseRepository {

	private final List<Prescription> prescriptions = new ArrayList<>();
	private final Map<String, Prescription> prescriptionsById = new LinkedHashMap<>();

	public void load(String filePath) {
		Objects.requireNonNull(filePath, "filePath");

		prescriptions.clear();
		prescriptionsById.clear();

		List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
		for (Map<String, String> row : rows) {
			Prescription prescription = mapRowToPrescription(row);
			add(prescription);
		}
	}

	public List<Prescription> findAll() {
		return new ArrayList<>(prescriptions);
	}

	public Prescription findById(String prescriptionId) {
		if (prescriptionId == null) {
			return null;
		}
		return prescriptionsById.get(prescriptionId);
	}

	public void add(Prescription prescription) {
		Objects.requireNonNull(prescription, "prescription");

		String prescriptionId = prescription.getId();
		if (prescriptionId == null || prescriptionId.trim().isEmpty()) {
			throw new IllegalArgumentException("Prescription id is required");
		}
		if (prescriptionsById.containsKey(prescriptionId)) {
			throw new IllegalArgumentException("Duplicate prescription id: " + prescriptionId);
		}

		prescriptions.add(prescription);
		prescriptionsById.put(prescriptionId, prescription);
	}

	public void update(Prescription prescription) {
		Objects.requireNonNull(prescription, "prescription");

		String prescriptionId = prescription.getId();
		if (prescriptionId == null || prescriptionId.trim().isEmpty()) {
			throw new IllegalArgumentException("Prescription id is required");
		}

		Prescription existing = prescriptionsById.get(prescriptionId);
		if (existing == null) {
			throw new IllegalArgumentException("Prescription not found: " + prescriptionId);
		}

		existing.setPatientId(prescription.getPatientId());
		existing.setClinicianId(prescription.getClinicianId());
		existing.setMedication(prescription.getMedication());
		existing.setDosage(prescription.getDosage());
		existing.setPharmacy(prescription.getPharmacy());
		existing.setCollectionStatus(prescription.getCollectionStatus());
		existing.setDateIssued(prescription.getDateIssued());
	}

	private Prescription mapRowToPrescription(Map<String, String> row) {
		String prescriptionId = value(row, "prescription_id");
		String patientId = value(row, "patient_id");
		String clinicianId = value(row, "clinician_id");
		String medicationName = value(row, "medication_name");
		String dosage = value(row, "dosage");
		String pharmacyName = value(row, "pharmacy_name");
		String status = value(row, "status");
		String issueDate = value(row, "issue_date");

		if (prescriptionId.isEmpty()) {
			throw new IllegalArgumentException("Prescription row is missing required field: prescription_id");
		}

		return new Prescription(prescriptionId, patientId, clinicianId, medicationName, dosage, pharmacyName, status,
				issueDate);
	}
}