package hms.view.referrals;

import hms.model.Referral;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReferralsTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Patient",
      "Referring Clinician",
      "Referred Clinician",
      "Referring Facility",
      "Referred Facility",
      "Urgency",
      "Status",
      "Referral Reason",
      "Clinical Summary",
      "Requested Investigations",
      "Notes",
      "Created Date"
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

    String patientId = safe(referral.getPatientId());
    String referringClinicianId = safe(referral.getReferringClinicianId());
    String referredClinicianId = safe(referral.getReferredToClinicianId());
    String referringFacilityId = safe(referral.getReferringFacilityId());
    String referredFacilityId = safe(referral.getReferredToFacilityId());

    if (columnIndex == 0) {
      return resolveNameOrId(patientId, patientNamesByPatientId, "Patient");
    }
    if (columnIndex == 1) {
      return resolveNameOrId(referringClinicianId, clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 2) {
      return resolveNameOrId(referredClinicianId, clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 3) {
      return resolveNameOrId(referringFacilityId, facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 4) {
      return resolveNameOrId(referredFacilityId, facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 5) {
      return safe(referral.getUrgencyLevel());
    }
    if (columnIndex == 6) {
      return referral.getStatus() == null ? "" : referral.getStatus().name();
    }
    if (columnIndex == 7) {
      return safe(referral.getReferralReason());
    }
    if (columnIndex == 8) {
      return safe(referral.getClinicalSummary());
    }
    if (columnIndex == 9) {
      return safe(referral.getRequestedInvestigations());
    }
    if (columnIndex == 10) {
      return safe(referral.getNotes());
    }
    if (columnIndex == 11) {
      return safe(referral.getCreatedDate());
    }

    return "";
  }

  private String resolveNameOrId(String id, Map<String, String> namesById, String entityLabel) {
    if (id.isEmpty()) {
      return "";
    }
    String name = namesById.getOrDefault(id, "");
    String safeName = safe(name);
    if (!safeName.isEmpty()) {
      return safeName;
    }
    return entityLabel + " " + id;
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}