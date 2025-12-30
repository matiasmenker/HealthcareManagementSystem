package hms.view.staff;

import hms.model.Staff;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaffTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Staff ID",
      "Full Name",
      "Email",
      "Role",
      "Facility ID",
      "Facility Name"
  };

  private List<Staff> staffMembers = new ArrayList<>();
  private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

  public void setStaffMembers(List<Staff> staffMembers, Map<String, String> facilityNamesByFacilityId) {
    this.staffMembers = staffMembers == null ? new ArrayList<>() : new ArrayList<>(staffMembers);
    this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(facilityNamesByFacilityId);
    fireTableDataChanged();
  }

  public Staff getStaffAtRow(int rowIndex) {
    if (rowIndex < 0 || rowIndex >= staffMembers.size()) {
      return null;
    }
    return staffMembers.get(rowIndex);
  }

  @Override
  public int getRowCount() {
    return staffMembers.size();
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
    Staff staff = getStaffAtRow(rowIndex);
    if (staff == null) {
      return "";
    }

    if (columnIndex == 0) {
      return staff.getId();
    }
    if (columnIndex == 1) {
      return staff.getFullName();
    }
    if (columnIndex == 2) {
      return staff.getEmail();
    }
    if (columnIndex == 3) {
      return staff.getRole();
    }
    if (columnIndex == 4) {
      return staff.getFacilityId();
    }
    if (columnIndex == 5) {
      return facilityNamesByFacilityId.getOrDefault(safe(staff.getFacilityId()), "");
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