package hms.view;

import hms.controller.AppointmentController;
import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.view.appointments.AppointmentsPanel;
import hms.view.clinicians.CliniciansPanel;
import hms.view.facilities.FacilitiesPanel;
import hms.view.patients.PatientsPanel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.util.Objects;

public class MainFrame extends JFrame {

  private final JLabel statusBarLabel;

  public MainFrame(PatientController patientController,
                   ClinicianController clinicianController,
                   AppointmentController appointmentController,
                   FacilityController facilityController) {
    Objects.requireNonNull(patientController, "patientController");
    Objects.requireNonNull(clinicianController, "clinicianController");
    Objects.requireNonNull(appointmentController, "appointmentController");
    Objects.requireNonNull(facilityController, "facilityController");

    setTitle("Healthcare Management System");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 650);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    statusBarLabel = new JLabel("Ready", SwingConstants.LEFT);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Patients", new PatientsPanel(patientController));
    tabbedPane.addTab("Clinicians", new CliniciansPanel(clinicianController));
    tabbedPane.addTab("Appointments", new AppointmentsPanel(appointmentController, patientController));
    tabbedPane.addTab("Prescriptions", new JLabel("Prescriptions screen not implemented yet"));
    tabbedPane.addTab("Referrals", new JLabel("Referrals screen not implemented yet"));
    tabbedPane.addTab("Facilities", new FacilitiesPanel(facilityController));
    tabbedPane.addTab("Staff", new JLabel("Staff screen not implemented yet"));

    add(tabbedPane, BorderLayout.CENTER);
    add(statusBarLabel, BorderLayout.SOUTH);
  }

  public void setStatusText(String text) {
    statusBarLabel.setText(text);
  }
}