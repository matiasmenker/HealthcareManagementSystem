package hms.repository;

import hms.model.Appointment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppointmentRepository {

  private final List<Appointment> appointments = new ArrayList<>();
  private final Map<String, Appointment> appointmentsById = new LinkedHashMap<>();

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
}