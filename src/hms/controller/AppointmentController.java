package hms.controller;

import hms.model.Appointment;
import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.repository.AppointmentRepository;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;

import java.util.List;
import java.util.Objects;

public class AppointmentController {

  private final AppointmentRepository appointmentRepository;
  private final PatientRepository patientRepository;
  private final ClinicianRepository clinicianRepository;
  private final FacilityRepository facilityRepository;

  public AppointmentController(AppointmentRepository appointmentRepository,
                               PatientRepository patientRepository,
                               ClinicianRepository clinicianRepository,
                               FacilityRepository facilityRepository) {
    this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository");
    this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
    this.clinicianRepository = Objects.requireNonNull(clinicianRepository, "clinicianRepository");
    this.facilityRepository = Objects.requireNonNull(facilityRepository, "facilityRepository");
  }

  public void loadAppointmentsFromCsv(String filePath) {
    appointmentRepository.load(filePath);
  }

  public List<Appointment> getAllAppointments() {
    return appointmentRepository.findAll();
  }

  public void addAppointment(Appointment appointment) {
    validateAppointment(appointment);
    appointmentRepository.add(appointment);
  }

  public void updateAppointment(Appointment appointment) {
    validateAppointment(appointment);
    appointmentRepository.update(appointment);
  }

  public void deleteAppointment(String appointmentId) {
    if (isBlank(appointmentId)) {
      throw new IllegalArgumentException("Appointment id is required");
    }
    appointmentRepository.delete(appointmentId);
  }

  private void validateAppointment(Appointment appointment) {
    Objects.requireNonNull(appointment, "appointment");

    if (isBlank(appointment.getId())) {
      throw new IllegalArgumentException("Appointment id is required");
    }
    if (isBlank(appointment.getPatientId())) {
      throw new IllegalArgumentException("Patient id is required");
    }
    if (isBlank(appointment.getClinicianId())) {
      throw new IllegalArgumentException("Clinician id is required");
    }
    if (isBlank(appointment.getFacilityId())) {
      throw new IllegalArgumentException("Facility id is required");
    }
    if (appointment.getStatus() == null) {
      throw new IllegalArgumentException("Appointment status is required");
    }
    if (isBlank(appointment.getDateTime())) {
      throw new IllegalArgumentException("Appointment date/time is required");
    }
    if (isBlank(appointment.getReason())) {
      throw new IllegalArgumentException("Appointment reason is required");
    }

    Patient patient = patientRepository.findById(appointment.getPatientId());
    if (patient == null) {
      throw new IllegalArgumentException("Patient not found: " + appointment.getPatientId());
    }

    Clinician clinician = clinicianRepository.findById(appointment.getClinicianId());
    if (clinician == null) {
      throw new IllegalArgumentException("Clinician not found: " + appointment.getClinicianId());
    }

    Facility facility = facilityRepository.findById(appointment.getFacilityId());
    if (facility == null) {
      throw new IllegalArgumentException("Facility not found: " + appointment.getFacilityId());
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}