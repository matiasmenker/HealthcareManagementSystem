package hms.view.clinicians;

import hms.model.Clinician;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CliniciansTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Clinician ID",
      "Full Name",
      "Email",
      "Role",
      "Qualification",
      "Facility ID",
      "Facility Name"
  };

  private List<Clinician> clinicians = new ArrayList<>();
  private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

  public void setClinicians(List<Clinician> clinicians, Map<String, String> facilityNamesByFacilityId) {
    this.clinicians = clinicians == null ? new ArrayList<>() : new ArrayList<>(clinicians);
    this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(facilityNamesByFacilityId);
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
      return clinician.getEmail();
    }
    if (columnIndex == 3) {
      return clinician.getRole();
    }
    if (columnIndex == 4) {
      return clinician.getQualification();
    }
    if (columnIndex == 5) {
      return clinician.getFacilityId();
    }
    if (columnIndex == 6) {
      return facilityNamesByFacilityId.getOrDefault(safe(clinician.getFacilityId()), "");
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