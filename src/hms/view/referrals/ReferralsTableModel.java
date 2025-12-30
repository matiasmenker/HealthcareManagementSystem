package hms.view.referrals;

import hms.model.Referral;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReferralsTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Referral ID",
      "Patient",
      "Referring Clinician",
      "Referred Clinician",
      "Referring Facility",
      "Referred Facility",
      "Urgency Level",
      "Status",
      "Referral Date",
      "Created Date",
      "Last Updated"
  };

  private List<Referral> referrals = new ArrayList<>();
  private Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
  private Map<String, String> clinicianNamesByClinicianId = new LinkedHashMap<>();
  private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

  public void setReferrals(List<Referral> referrals,
                           Map<String, String> patientNamesByPatientId,
                           Map<String, String> clinicianNamesByClinicianId,
                           Map<String, String> facilityNamesByFacilityId) {
    this.referrals = referrals == null ? new ArrayList<>() : new ArrayList<>(referrals);
    this.patientNamesByPatientId = patientNamesByPatientId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(patientNamesByPatientId);
    this.clinicianNamesByClinicianId = clinicianNamesByClinicianId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(clinicianNamesByClinicianId);
    this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(facilityNamesByFacilityId);
    fireTableDataChanged();
  }

  public Referral getReferralAtRow(int rowIndex) {
    if (rowIndex < 0 || rowIndex >= referrals.size()) {
      return null;
    }
    return referrals.get(rowIndex);
  }

  @Override
  public int getRowCount() {
    return referrals.size();
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
    Referral referral = getReferralAtRow(rowIndex);
    if (referral == null) {
      return "";
    }

    if (columnIndex == 0) {
      return safe(referral.getId());
    }
    if (columnIndex == 1) {
      return resolveNameOrFallbackId(safe(referral.getPatientId()), patientNamesByPatientId, "Patient");
    }
    if (columnIndex == 2) {
      return resolveNameOrFallbackId(safe(referral.getReferringClinicianId()), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 3) {
      return resolveNameOrFallbackId(safe(referral.getReferredToClinicianId()), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 4) {
      return resolveNameOrFallbackId(safe(referral.getReferringFacilityId()), facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 5) {
      return resolveNameOrFallbackId(safe(referral.getReferredToFacilityId()), facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 6) {
      return safe(referral.getUrgencyLevel());
    }
    if (columnIndex == 7) {
      return referral.getStatus() == null ? "" : referral.getStatus().name();
    }
    if (columnIndex == 8) {
      return safe(referral.getReferralDate());
    }
    if (columnIndex == 9) {
      return safe(referral.getCreatedDate());
    }
    if (columnIndex == 10) {
      return safe(referral.getLastUpdated());
    }

    return "";
  }

  private String resolveNameOrFallbackId(String id, Map<String, String> namesById, String label) {
    if (id.isEmpty()) {
      return "";
    }
    String name = safe(namesById.get(id));
    if (!name.isEmpty()) {
      return name;
    }
    return label + " " + id;
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}