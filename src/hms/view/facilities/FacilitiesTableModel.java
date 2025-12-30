package hms.view.facilities;

import hms.model.Facility;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FacilitiesTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Facility ID",
      "Facility Name",
      "Facility Type",
      "Address",
      "Postcode",
      "Phone Number",
      "Email",
      "Opening Hours",
      "Manager Name",
      "Capacity",
      "Specialities Offered"
  };

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
      return safe(facility.getId());
    }
    if (columnIndex == 1) {
      return safe(facility.getName());
    }
    if (columnIndex == 2) {
      return safe(facility.getType());
    }
    if (columnIndex == 3) {
      return safe(facility.getAddress());
    }
    if (columnIndex == 4) {
      return safe(facility.getPostcode());
    }
    if (columnIndex == 5) {
      return safe(facility.getPhoneNumber());
    }
    if (columnIndex == 6) {
      return safe(facility.getEmail());
    }
    if (columnIndex == 7) {
      return safe(facility.getOpeningHours());
    }
    if (columnIndex == 8) {
      return safe(facility.getManagerName());
    }
    if (columnIndex == 9) {
      return safe(facility.getCapacity());
    }
    if (columnIndex == 10) {
      return safe(facility.getSpecialitiesOffered());
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