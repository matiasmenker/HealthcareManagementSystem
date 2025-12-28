package hms.controller;

import hms.model.Appointment;
import hms.repository.AppointmentRepository;

import java.util.List;
import java.util.Objects;

public class AppointmentController {

  private final AppointmentRepository appointmentRepository;

  public AppointmentController(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository");
  }

  public List<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  public void addAppointment(Appointment appointment) {
    Objects.requireNonNull(appointment, "appointment");
    appointmentRepository.add(appointment);
  }

  public void updateAppointment(Appointment appointment) {
    Objects.requireNonNull(appointment, "appointment");
    appointmentRepository.update(appointment);
  }
}