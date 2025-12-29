package hms.view.patients;

import hms.model.Patient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PatientsTableModel extends AbstractTableModel {

	private final String[] columnNames = new String[] { "Patient ID", "Full Name", "Email", "NHS Number",
			"Phone Number", "Address", "GP Surgery ID" };

	private List<Patient> patients = new ArrayList<>();

	public void setPatients(List<Patient> patients) {
		if (patients == null) {
			this.patients = new ArrayList<>();
		} else {
			this.patients = new ArrayList<>(patients);
		}
		fireTableDataChanged();
	}

	public Patient getPatientAtRow(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= patients.size()) {
			return null;
		}
		return patients.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return patients.size();
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
		Patient patient = patients.get(rowIndex);

		if (columnIndex == 0) {
			return patient.getId();
		}
		if (columnIndex == 1) {
			return patient.getFullName();
		}
		if (columnIndex == 2) {
			return patient.getEmail();
		}
		if (columnIndex == 3) {
			return patient.getNhsNumber();
		}
		if (columnIndex == 4) {
			return patient.getPhone();
		}
		if (columnIndex == 5) {
			return patient.getAddress();
		}
		if (columnIndex == 6) {
			return patient.getRegisteredFacilityId();
		}

		return "";
	}
}