package hms.view.clinicians;

import hms.model.Clinician;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CliniciansTableModel extends AbstractTableModel {

	private final String[] columnNames = new String[] { "Clinician ID", "Full Name", "Title", "Specialty",
			"Registration Number", "Phone", "Email", "Workplace ID", "Workplace Type", "Employment Status",
			"Start Date" };

	private List<Clinician> clinicians = new ArrayList<>();

	public void setClinicians(List<Clinician> clinicians) {
		this.clinicians = clinicians == null ? new ArrayList<>() : new ArrayList<>(clinicians);
		fireTableDataChanged();
	}

	public Clinician getClinicianAtRow(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= clinicians.size()) {
			return null;
		}
		return clinicians.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return clinicians.size();
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
		Clinician clinician = getClinicianAtRow(rowIndex);
		if (clinician == null) {
			return "";
		}

		if (columnIndex == 0) {
			return clinician.getId();
		}
		if (columnIndex == 1) {
			return clinician.getFullName();
		}
		if (columnIndex == 2) {
			return clinician.getTitle();
		}
		if (columnIndex == 3) {
			return clinician.getSpecialty();
		}
		if (columnIndex == 4) {
			return clinician.getMedicalRegistrationNumber();
		}
		if (columnIndex == 5) {
			return clinician.getPhoneNumber();
		}
		if (columnIndex == 6) {
			return clinician.getEmail();
		}
		if (columnIndex == 7) {
			return clinician.getWorkplaceId();
		}
		if (columnIndex == 8) {
			return clinician.getWorkplaceType();
		}
		if (columnIndex == 9) {
			return clinician.getEmploymentStatus();
		}
		if (columnIndex == 10) {
			return clinician.getStartDate();
		}

		return "";
	}
}