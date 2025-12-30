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
      "Patient ID",
      "Patient Name",
      "From Clinician ID",
      "From Clinician Name",
      "To Clinician ID",
      "To Clinician Name",
      "From Facility ID",
      "From Facility Name",
      "To Facility ID",
      "To Facility Name",
      "Urgency",
      "Status",
      "Referral Date",
      "Reason"
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
      return referral.getId();
    }
    if (columnIndex == 1) {
      return referral.getPatientId();
    }
    if (columnIndex == 2) {
      return patientNamesByPatientId.getOrDefault(safe(referral.getPatientId()), "");
    }
    if (columnIndex == 3) {
      return referral.getReferringClinicianId();
    }
    if (columnIndex == 4) {
      return clinicianNamesByClinicianId.getOrDefault(safe(referral.getReferringClinicianId()), "");
    }
    if (columnIndex == 5) {
      return referral.getReferredToClinicianId();
    }
    if (columnIndex == 6) {
      return clinicianNamesByClinicianId.getOrDefault(safe(referral.getReferredToClinicianId()), "");
    }
    if (columnIndex == 7) {
      return referral.getReferringFacilityId();
    }
    if (columnIndex == 8) {
      return facilityNamesByFacilityId.getOrDefault(safe(referral.getReferringFacilityId()), "");
    }
    if (columnIndex == 9) {
      return referral.getReferredToFacilityId();
    }
    if (columnIndex == 10) {
      return facilityNamesByFacilityId.getOrDefault(safe(referral.getReferredToFacilityId()), "");
    }
    if (columnIndex == 11) {
      return referral.getUrgencyLevel();
    }
    if (columnIndex == 12) {
      return referral.getStatus() == null ? "" : referral.getStatus().name();
    }
    if (columnIndex == 13) {
      return referral.getReferralDate();
    }
    if (columnIndex == 14) {
      return referral.getReferralReason();
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