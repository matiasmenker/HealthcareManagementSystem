package hms.view;

import hms.controller.AppointmentController;
import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.controller.PrescriptionController;
import hms.controller.ReferralController;
import hms.controller.StaffController;
import hms.view.appointments.AppointmentsPanel;
import hms.view.clinicians.CliniciansPanel;
import hms.view.facilities.FacilitiesPanel;
import hms.view.patients.PatientsPanel;
import hms.view.prescriptions.PrescriptionsPanel;
import hms.view.referrals.ReferralsPanel;
import hms.view.staff.StaffPanel;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MainFrame extends JFrame {

  private final JTextArea logTextArea;
  private final DateTimeFormatter timestampFormatter;

  public MainFrame(PatientController patientController,
                   ClinicianController clinicianController,
                   AppointmentController appointmentController,
                   FacilityController facilityController,
                   StaffController staffController,
                   PrescriptionController prescriptionController,
                   ReferralController referralController) {
    Objects.requireNonNull(patientController, "patientController");
    Objects.requireNonNull(clinicianController, "clinicianController");
    Objects.requireNonNull(appointmentController, "appointmentController");
    Objects.requireNonNull(facilityController, "facilityController");
    Objects.requireNonNull(staffController, "staffController");
    Objects.requireNonNull(prescriptionController, "prescriptionController");
    Objects.requireNonNull(referralController, "referralController");

    this.timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    setTitle("Healthcare Management System");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1250, 780);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Patients", new PatientsPanel(patientController, facilityController));
    tabbedPane.addTab("Clinicians", new CliniciansPanel(clinicianController, facilityController));
    tabbedPane.addTab("Appointments", new AppointmentsPanel(appointmentController, patientController, clinicianController, facilityController));
    tabbedPane.addTab("Prescriptions", new PrescriptionsPanel(prescriptionController, patientController, clinicianController));
    tabbedPane.addTab("Referrals", new ReferralsPanel(referralController, patientController, clinicianController, facilityController));
    tabbedPane.addTab("Facilities", new FacilitiesPanel(facilityController));
    tabbedPane.addTab("Staff", new StaffPanel(staffController, facilityController));

    logTextArea = new JTextArea();
    logTextArea.setEditable(false);
    logTextArea.setLineWrap(true);
    logTextArea.setWrapStyleWord(true);

    JScrollPane logScrollPane = new JScrollPane(logTextArea);
    logScrollPane.setBorder(BorderFactory.createTitledBorder("Application Logs"));
    logScrollPane.setPreferredSize(new Dimension(100, 170));

    add(tabbedPane, BorderLayout.CENTER);
    add(logScrollPane, BorderLayout.SOUTH);
  }

  public void setStatusText(String text) {
    appendLogLine(text);
  }

  public void appendLogLine(String text) {
    String safeText = text == null ? "" : text.trim();
    String timestamp = LocalDateTime.now().format(timestampFormatter);
    String line = timestamp + " " + safeText;

    SwingUtilities.invokeLater(() -> {
      logTextArea.append(line);
      logTextArea.append(System.lineSeparator());
      logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    });
  }
}