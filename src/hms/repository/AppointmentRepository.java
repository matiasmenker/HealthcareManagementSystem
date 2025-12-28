package hms.repository;

import hms.model.Appointment;
import hms.model.enums.AppointmentStatus;
import hms.util.CsvFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppointmentRepository {

  private final List<Appointment> appointments = new ArrayList<>();
  private final Map<String, Appointment> appointmentsById = new LinkedHashMap<>();

  public void load(String filePath) {
    Objects.requireNonNull(filePath, "filePath");

    appointments.clear();
    appointmentsById.clear();

    List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
    for (Map<String, String> row : rows) {
      Appointment appointment = mapRowToAppointment(row);
      add(appointment);
    }
  }

  public List<Appointment> findAll() {
    return new ArrayList<>(appointments);
  }

  public Appointment findById(String id) {
    if (id == null) {
      return null;
    }
    return appointmentsById.get(id);
  }

  public void add(Appointment appointment) {
    Objects.requireNonNull(appointment, "appointment");

    String id = appointment.getId();
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Appointment id is required");
    }
    if (appointmentsById.containsKey(id)) {
      throw new IllegalArgumentException("Duplicate appointment id: " + id);
    }

    appointments.add(appointment);
    appointmentsById.put(id, appointment);
  }

  public void update(Appointment appointment) {
    Objects.requireNonNull(appointment, "appointment");

    String id = appointment.getId();
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Appointment id is required");
    }

    Appointment existing = appointmentsById.get(id);
    if (existing == null) {
      throw new IllegalArgumentException("Appointment not found: " + id);
    }

    existing.setPatientId(appointment.getPatientId());
    existing.setClinicianId(appointment.getClinicianId());
    existing.setFacilityId(appointment.getFacilityId());
    existing.setDateTime(appointment.getDateTime());
    existing.setStatus(appointment.getStatus());
    existing.setReason(appointment.getReason());
  }

  private Appointment mapRowToAppointment(Map<String, String> row) {
    String appointmentId = value(row, "appointment_id");
    String patientId = value(row, "patient_id");
    String clinicianId = value(row, "clinician_id");
    String facilityId = value(row, "facility_id");
    String appointmentDate = value(row, "appointment_date");
    String appointmentTime = value(row, "appointment_time");
    String status = value(row, "status");
    String reason = value(row, "reason_for_visit");

    if (appointmentId.isEmpty()) {
      throw new IllegalArgumentException("Appointment row is missing required field: appointment_id");
    }

    String dateTime = (appointmentDate + " " + appointmentTime).trim();

    return new Appointment(
        appointmentId,
        patientId,
        clinicianId,
        facilityId,
        dateTime,
        parseAppointmentStatus(status),
        reason
    );
  }

  private AppointmentStatus parseAppointmentStatus(String rawStatus) {
    if (rawStatus == null) {
      return AppointmentStatus.SCHEDULED;
    }

    String normalized = rawStatus.trim().toUpperCase();
    if (normalized.isEmpty()) {
      return AppointmentStatus.SCHEDULED;
    }

    if (normalized.equals("CANCELED")) {
      normalized = "CANCELLED";
    }

    try {
      return AppointmentStatus.valueOf(normalized);
    } catch (IllegalArgumentException exception) {
      return AppointmentStatus.SCHEDULED;
    }
  }

  private String value(Map<String, String> row, String key) {
    if (row == null || key == null) {
      return "";
    }
    String v = row.get(key);
    return v == null ? "" : v.trim();
  }
}