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
      "Patient ID",
      "Patient Name",
      "Clinician ID",
      "Facility ID",
      "Date Time",
      "Status",
      "Reason"
  };

  private List<Appointment> appointments = new ArrayList<>();
  private Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();

  public void setAppointments(List<Appointment> appointments, Map<String, String> patientNamesByPatientId) {
    this.appointments = appointments == null ? new ArrayList<>() : new ArrayList<>(appointments);
    this.patientNamesByPatientId = patientNamesByPatientId == null ? new LinkedHashMap<>() : new LinkedHashMap<>(patientNamesByPatientId);
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
      return appointment.getId();
    }
    if (columnIndex == 1) {
      return appointment.getPatientId();
    }
    if (columnIndex == 2) {
      String patientId = appointment.getPatientId();
      if (patientId == null || patientId.trim().isEmpty()) {
        return "";
      }
      return patientNamesByPatientId.getOrDefault(patientId, "");
    }
    if (columnIndex == 3) {
      return appointment.getClinicianId();
    }
    if (columnIndex == 4) {
      return appointment.getFacilityId();
    }
    if (columnIndex == 5) {
      return appointment.getDateTime();
    }
    if (columnIndex == 6) {
      return appointment.getStatus() == null ? "" : appointment.getStatus().name();
    }
    if (columnIndex == 7) {
      return appointment.getReason();
    }

    return "";
  }
}