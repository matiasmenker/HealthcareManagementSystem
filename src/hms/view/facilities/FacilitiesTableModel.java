package hms.view.facilities;

import hms.model.Facility;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FacilitiesTableModel extends AbstractTableModel {

	private final String[] columnNames = new String[] { "Facility ID", "Name", "Type", "Address", "Postcode", "Phone",
			"Email", "Opening Hours", "Manager", "Capacity", "Specialities" };

	private List<Facility> facilities = new ArrayList<>();

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities == null ? new ArrayList<>() : new ArrayList<>(facilities);
		fireTableDataChanged();
	}

	public Facility getFacilityAtRow(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= facilities.size()) {
			return null;
		}
		return facilities.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return facilities.size();
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
		Facility facility = getFacilityAtRow(rowIndex);
		if (facility == null) {
			return "";
		}

		if (columnIndex == 0) {
			return facility.getId();
		}
		if (columnIndex == 1) {
			return facility.getName();
		}
		if (columnIndex == 2) {
			return facility.getType();
		}
		if (columnIndex == 3) {
			return facility.getAddress();
		}
		if (columnIndex == 4) {
			return facility.getPostcode();
		}
		if (columnIndex == 5) {
			return facility.getPhoneNumber();
		}
		if (columnIndex == 6) {
			return facility.getEmail();
		}
		if (columnIndex == 7) {
			return facility.getOpeningHours();
		}
		if (columnIndex == 8) {
			return facility.getManagerName();
		}
		if (columnIndex == 9) {
			return facility.getCapacity();
		}
		if (columnIndex == 10) {
			return facility.getSpecialitiesOffered();
		}

		return "";
	}
}