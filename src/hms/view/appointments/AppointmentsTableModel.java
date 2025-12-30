package hms.view.appointments;

import hms.model.Appointment;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppointmentsTableModel extends AbstractTableModel {

  private final String[] columnNames = new String[] {
      "Appointment ID",
      "Date/Time",
      "Patient",
      "Clinician",
      "Facility",
      "Status",
      "Reason"
  };

  private List<Appointment> appointments = new ArrayList<>();
  private Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
  private Map<String, String> clinicianNamesByClinicianId = new LinkedHashMap<>();
  private Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();

  public void setAppointments(List<Appointment> appointments,
                              Map<String, String> patientNamesByPatientId,
                              Map<String, String> clinicianNamesByClinicianId,
                              Map<String, String> facilityNamesByFacilityId) {
    this.appointments = appointments == null ? new ArrayList<>() : new ArrayList<>(appointments);
    this.patientNamesByPatientId = patientNamesByPatientId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(patientNamesByPatientId);
    this.clinicianNamesByClinicianId = clinicianNamesByClinicianId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(clinicianNamesByClinicianId);
    this.facilityNamesByFacilityId = facilityNamesByFacilityId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(facilityNamesByFacilityId);
    fireTableDataChanged();
  }

  public Appointment getAppointmentAtRow(int rowIndex) {
    if (rowIndex < 0 || rowIndex >= appointments.size()) {
      return null;
    }
    return appointments.get(rowIndex);
  }

  @Override
  public int getRowCount() {
    return appointments.size();
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
    Appointment appointment = getAppointmentAtRow(rowIndex);
    if (appointment == null) {
      return "";
    }

    if (columnIndex == 0) {
      return safe(appointment.getId());
    }
    if (columnIndex == 1) {
      return safe(appointment.getDateTime());
    }
    if (columnIndex == 2) {
      return resolveNameOrFallbackId(safe(appointment.getPatientId()), patientNamesByPatientId, "Patient");
    }
    if (columnIndex == 3) {
      return resolveNameOrFallbackId(safe(appointment.getClinicianId()), clinicianNamesByClinicianId, "Clinician");
    }
    if (columnIndex == 4) {
      return resolveNameOrFallbackId(safe(appointment.getFacilityId()), facilityNamesByFacilityId, "Facility");
    }
    if (columnIndex == 5) {
      return appointment.getStatus() == null ? "" : appointment.getStatus().name();
    }
    if (columnIndex == 6) {
      return safe(appointment.getReason());
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