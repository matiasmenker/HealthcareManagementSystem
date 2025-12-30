package hms.view.prescriptions;

import hms.model.Prescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionsTableModel extends AbstractTableModel {

	private final String[] columnNames = new String[] { "Prescription ID", "Patient ID", "Patient Name", "Clinician ID",
			"Clinician Name", "Medication", "Dosage", "Pharmacy", "Status", "Date Issued" };

	private List<Prescription> prescriptions = new ArrayList<>();
	private Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
	private Map<String, String> clinicianNamesByClinicianId = new LinkedHashMap<>();

	public void setPrescriptions(List<Prescription> prescriptions, Map<String, String> patientNamesByPatientId,
			Map<String, String> clinicianNamesByClinicianId) {
		this.prescriptions = prescriptions == null ? new ArrayList<>() : new ArrayList<>(prescriptions);
		this.patientNamesByPatientId = patientNamesByPatientId == null ? new LinkedHashMap<>()
				: new LinkedHashMap<>(patientNamesByPatientId);
		this.clinicianNamesByClinicianId = clinicianNamesByClinicianId == null ? new LinkedHashMap<>()
				: new LinkedHashMap<>(clinicianNamesByClinicianId);
		fireTableDataChanged();
	}

	public Prescription getPrescriptionAtRow(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= prescriptions.size()) {
			return null;
		}
		return prescriptions.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return prescriptions.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Prescription prescription = getPrescriptionAtRow(rowIndex);
		if (prescription == null) {
			return "";
		}

		if (columnIndex == 0) {
			return prescription.getId();
		}
		if (columnIndex == 1) {
			return prescription.getPatientId();
		}
		if (columnIndex == 2) {
			String patientId = prescription.getPatientId();
			if (patientId == null || patientId.trim().isEmpty()) {
				return "";
			}
			return patientNamesByPatientId.getOrDefault(patientId, "");
		}
		if (columnIndex == 3) {
			return prescription.getClinicianId();
		}
		if (columnIndex == 4) {
			String clinicianId = prescription.getClinicianId();
			if (clinicianId == null || clinicianId.trim().isEmpty()) {
				return "";
			}
			return clinicianNamesByClinicianId.getOrDefault(clinicianId, "");
		}
		if (columnIndex == 5) {
			return prescription.getMedication();
		}
		if (columnIndex == 6) {
			return prescription.getDosage();
		}
		if (columnIndex == 7) {
			return prescription.getPharmacy();
		}
		if (columnIndex == 8) {
			return prescription.getCollectionStatus();
		}
		if (columnIndex == 9) {
			return prescription.getDateIssued();
		}

		return "";
	}
}