package hms.view.referrals;

import hms.model.Referral;
import hms.model.enums.ReferralStatus;

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
      "Referral Date",
      "Urgency Level",
      "Status",
      "Referral Reason",
      "Clinical Summary",
      "Requested Investigations",
      "Notes"
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
      return resolveLabel(referral.getPatientId(), patientNamesByPatientId, "Patient");
    }
    if (columnIndex == 2) {
      return resolveLabel(referral.getReferringClinicianId(), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 3) {
      return resolveLabel(referral.getReferredToClinicianId(), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 4) {
      return resolveLabel(referral.getReferringFacilityId(), facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 5) {
      return resolveLabel(referral.getReferredToFacilityId(), facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 6) {
      return safe(referral.getReferralDate());
    }
    if (columnIndex == 7) {
      return safe(referral.getUrgencyLevel());
    }
    if (columnIndex == 8) {
      ReferralStatus status = referral.getStatus();
      return status == null ? "" : status.name();
    }
    if (columnIndex == 9) {
      return safe(referral.getReferralReason());
    }
    if (columnIndex == 10) {
      return safe(referral.getClinicalSummary());
    }
    if (columnIndex == 11) {
      return safe(referral.getRequestedInvestigations());
    }
    if (columnIndex == 12) {
      return safe(referral.getNotes());
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