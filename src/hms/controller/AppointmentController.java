package hms.controller;

import hms.model.Appointment;
import hms.model.Patient;
import hms.repository.AppointmentRepository;
import hms.repository.PatientRepository;

import java.util.List;
import java.util.Objects;

public class AppointmentController {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;

	public AppointmentController(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
		this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository");
		this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository");
	}

	public void loadAppointmentsFromCsv(String filePath) {
		appointmentRepository.load(filePath);
	}

	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	public void addAppointment(Appointment appointment) {
		Objects.requireNonNull(appointment, "appointment");
		validateAppointmentReferences(appointment);
		appointmentRepository.add(appointment);
	}

	public void updateAppointment(Appointment appointment) {
		Objects.requireNonNull(appointment, "appointment");
		validateAppointmentReferences(appointment);
		appointmentRepository.update(appointment);
	}

	private void validateAppointmentReferences(Appointment appointment) {
		String patientId = appointment.getPatientId();
		if (patientId == null || patientId.trim().isEmpty()) {
			throw new IllegalArgumentException("Patient id is required");
		}

		Patient patient = patientRepository.findById(patientId);
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found: " + patientId);
		}
	}
}