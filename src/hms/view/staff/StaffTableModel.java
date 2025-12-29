package hms.view.staff;

import hms.model.Staff;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StaffTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Staff ID",
      "Full Name",
      "Role",
      "Department",
      "Phone",
      "Email",
      "Work Location",
      "Shift Pattern",
      "Employment Status",
      "Start Date",
      "Supervisor",
      "Access Level"
  };

  private List<Staff> staffMembers = new ArrayList<>();

  public void setStaffMembers(List<Staff> staffMembers) {
    this.staffMembers = staffMembers == null ? new ArrayList<>() : new ArrayList<>(staffMembers);
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
      return staff.getRole();
    }
    if (columnIndex == 3) {
      return staff.getDepartment();
    }
    if (columnIndex == 4) {
      return staff.getPhoneNumber();
    }
    if (columnIndex == 5) {
      return staff.getEmail();
    }
    if (columnIndex == 6) {
      return staff.getWorkLocation();
    }
    if (columnIndex == 7) {
      return staff.getShiftPattern();
    }
    if (columnIndex == 8) {
      return staff.getEmploymentStatus();
    }
    if (columnIndex == 9) {
      return staff.getStartDate();
    }
    if (columnIndex == 10) {
      return staff.getSupervisorName();
    }
    if (columnIndex == 11) {
      return staff.getAccessLevel();
    }

    return "";
  }
}