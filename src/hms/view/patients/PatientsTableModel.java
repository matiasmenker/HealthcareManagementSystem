package hms.view.patients;

import hms.model.Patient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatientsTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Patient ID",
      "Full Name",
      "Email",
      "NHS Number",
      "Phone",
      "Address",
      "Registered Facility ID",
      "Registered Facility Name"
  };

  private List<Patient> patients = new ArrayList<>();
  private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

  public void setPatients(List<Patient> patients, Map<String, String> facilityNamesByFacilityId) {
    this.patients = patients == null ? new ArrayList<>() : new ArrayList<>(patients);
    this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(facilityNamesByFacilityId);
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
    Patient patient = getPatientAtRow(rowIndex);
    if (patient == null) {
      return "";
    }

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
    if (columnIndex == 7) {
      return facilityNamesByFacilityId.getOrDefault(safe(patient.getRegisteredFacilityId()), "");
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