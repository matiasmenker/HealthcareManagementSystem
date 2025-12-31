package hms.view.clinicians;

import hms.model.Clinician;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CliniciansTableModel extends AbstractTableModel {

	private final String[] columnNames = new String[] { "Clinician ID", "First Name", "Last Name", "Title",
			"Speciality", "GMC Number", "Phone Number", "Email", "Workplace", "Employment Status", "Start Date" };

	private List<Clinician> clinicians = new ArrayList<>();
	private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

	public void setClinicians(List<Clinician> clinicians, Map<String, String> facilityNamesByFacilityId) {
		this.clinicians = clinicians == null ? new ArrayList<>() : new ArrayList<>(clinicians);
		this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>()
				: new LinkedHashMap<>(facilityNamesByFacilityId);
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
			return safe(clinician.getId());
		}
		if (columnIndex == 1) {
			return safe(clinician.getFirstName());
		}
		if (columnIndex == 2) {
			return safe(clinician.getLastName());
		}
		if (columnIndex == 3) {
			return safe(clinician.getTitle());
		}
		if (columnIndex == 4) {
			return safe(clinician.getSpeciality());
		}
		if (columnIndex == 5) {
			return safe(clinician.getGmcNumber());
		}
		if (columnIndex == 6) {
			return safe(clinician.getPhoneNumber());
		}
		if (columnIndex == 7) {
			return safe(clinician.getEmail());
		}
		if (columnIndex == 8) {
			String workplaceId = safe(clinician.getWorkplaceId());
			if (workplaceId.isEmpty()) {
				return "";
			}
			String name = safe(facilityNamesByFacilityId.get(workplaceId));
			if (!name.isEmpty()) {
				return name;
			}
			return "Facility " + workplaceId;
		}
		if (columnIndex == 9) {
			return safe(clinician.getEmploymentStatus());
		}
		if (columnIndex == 10) {
			return safe(clinician.getStartDate());
		}

		return "";
	}

	private String safe(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}