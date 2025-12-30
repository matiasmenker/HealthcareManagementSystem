package hms.view.prescriptions;

import hms.model.Prescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionsTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Prescription ID",
      "Patient",
      "Clinician",
      "Medication",
      "Dosage",
      "Pharmacy",
      "Status",
      "Date Issued"
  };

  private List<Prescription> prescriptions = new ArrayList<>();
  private Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
  private Map<String, String> clinicianNamesByClinicianId = new LinkedHashMap<>();

  public void setPrescriptions(List<Prescription> prescriptions,
                               Map<String, String> patientNamesByPatientId,
                               Map<String, String> clinicianNamesByClinicianId) {
    this.prescriptions = prescriptions == null ? new ArrayList<>() : new ArrayList<>(prescriptions);
    this.patientNamesByPatientId = patientNamesByPatientId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(patientNamesByPatientId);
    this.clinicianNamesByClinicianId = clinicianNamesByClinicianId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(clinicianNamesByClinicianId);
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
      return safe(prescription.getId());
    }
    if (columnIndex == 1) {
      return resolveLabel(prescription.getPatientId(), patientNamesByPatientId, "Patient");
    }
    if (columnIndex == 2) {
      return resolveLabel(prescription.getClinicianId(), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 3) {
      return safe(prescription.getMedication());
    }
    if (columnIndex == 4) {
      return safe(prescription.getDosage());
    }
    if (columnIndex == 5) {
      return safe(prescription.getPharmacy());
    }
    if (columnIndex == 6) {
      return safe(prescription.getCollectionStatus());
    }
    if (columnIndex == 7) {
      return safe(prescription.getDateIssued());
    }

    return "";
  }

  private String resolveLabel(String id, Map<String, String> namesById, String fallbackPrefix) {
    String safeId = safe(id);
    if (safeId.isEmpty()) {
      return "";
    }
    String name = safe(namesById.get(safeId));
    if (!name.isEmpty()) {
      return name;
    }
    return fallbackPrefix + " " + safeId;
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}