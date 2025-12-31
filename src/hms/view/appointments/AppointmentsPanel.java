package hms.view.appointments;

import hms.controller.AppointmentController;
import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.model.Appointment;
import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.enums.AppointmentStatus;
import hms.view.common.ActionFeedbackNotifier;
import hms.view.common.ButtonsActionsBar;
import hms.view.common.FormDialog;
import hms.view.common.FormFieldViewConfiguration;
import hms.view.common.SelectionItem;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Window;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppointmentsPanel extends JPanel {

  private final AppointmentController appointmentController;
  private final PatientController patientController;
  private final ClinicianController clinicianController;
  private final FacilityController facilityController;

  private final AppointmentsTableModel appointmentsTableModel;
  private final JTable appointmentsTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public AppointmentsPanel(AppointmentController appointmentController,
                           PatientController patientController,
                           ClinicianController clinicianController,
                           FacilityController facilityController) {
    this.appointmentController = Objects.requireNonNull(appointmentController, "appointmentController");
    this.patientController = Objects.requireNonNull(patientController, "patientController");
    this.clinicianController = Objects.requireNonNull(clinicianController, "clinicianController");
    this.facilityController = Objects.requireNonNull(facilityController, "facilityController");

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    appointmentsTableModel = new AppointmentsTableModel();
    appointmentsTable = new JTable(appointmentsTableModel);
    appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
      @Override
      public void onAdd() {
        openAddAppointmentDialog();
      }

      @Override
      public void onEdit() {
        openEditAppointmentDialog();
      }

      @Override
      public void onRefresh() {
        refreshAppointmentsTable();
      }
    });

    buttonsActionsBar.setEditEnabled(false);

    appointmentsTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = appointmentsTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);

    refreshAppointmentsTable();
  }

  private void refreshAppointmentsTable() {
    try {
      List<Appointment> appointments = appointmentController.getAllAppointments();
      Map<String, String> patientNamesByPatientId = buildPatientNamesByPatientId();
      Map<String, String> clinicianNamesByClinicianId = buildClinicianNamesByClinicianId();
      Map<String, String> facilityNamesByFacilityId = buildFacilityNamesByFacilityId();
      appointmentsTableModel.setAppointments(appointments, patientNamesByPatientId, clinicianNamesByClinicianId, facilityNamesByFacilityId);
      buttonsActionsBar.setEditEnabled(appointmentsTable.getSelectedRow() >= 0);
      ActionFeedbackNotifier.showInformation(this, "Appointments list refreshed");
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddAppointmentDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> patientOptions = buildPatientSelectionItems();
      List<SelectionItem> clinicianOptions = buildClinicianSelectionItems();
      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();
      List<SelectionItem> statusOptions = buildAppointmentStatusSelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("appointmentId", "Appointment ID"),
          FormFieldViewConfiguration.requiredSelect("patientId", "Patient", patientOptions),
          FormFieldViewConfiguration.requiredSelect("clinicianId", "Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("facilityId", "Facility", facilityOptions),
          FormFieldViewConfiguration.requiredEditable("dateTime", "Date/Time"),
          FormFieldViewConfiguration.requiredSelect("status", "Status", statusOptions),
          FormFieldViewConfiguration.requiredEditable("reason", "Reason")
      );

      FormDialog formDialog = new FormDialog(owner, "Add Appointment", fieldViewConfigurations, Map.of());
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Appointment appointment = new Appointment(
          valuesByKey.getOrDefault("appointmentId", ""),
          valuesByKey.getOrDefault("patientId", ""),
          valuesByKey.getOrDefault("clinicianId", ""),
          valuesByKey.getOrDefault("facilityId", ""),
          valuesByKey.getOrDefault("dateTime", ""),
          parseAppointmentStatus(valuesByKey.getOrDefault("status", "")),
          valuesByKey.getOrDefault("reason", "")
      );

      appointmentController.addAppointment(appointment);
      refreshAppointmentsTable();
      selectAppointmentById(appointment.getId());
      ActionFeedbackNotifier.showSuccess(this, "Appointment created: " + safe(appointment.getId()));
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openEditAppointmentDialog() {
    int selectedRowIndex = appointmentsTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select an appointment to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Appointment selectedAppointment = appointmentsTableModel.getAppointmentAtRow(selectedRowIndex);
    if (selectedAppointment == null) {
      JOptionPane.showMessageDialog(this, "Select an appointment to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> patientOptions = buildPatientSelectionItems();
      List<SelectionItem> clinicianOptions = buildClinicianSelectionItems();
      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();
      List<SelectionItem> statusOptions = buildAppointmentStatusSelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("appointmentId", "Appointment ID"),
          FormFieldViewConfiguration.requiredSelect("patientId", "Patient", patientOptions),
          FormFieldViewConfiguration.requiredSelect("clinicianId", "Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("facilityId", "Facility", facilityOptions),
          FormFieldViewConfiguration.requiredEditable("dateTime", "Date/Time"),
          FormFieldViewConfiguration.requiredSelect("status", "Status", statusOptions),
          FormFieldViewConfiguration.requiredEditable("reason", "Reason")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("appointmentId", selectedAppointment.getId());
      defaultValuesByKey.put("patientId", selectedAppointment.getPatientId());
      defaultValuesByKey.put("clinicianId", selectedAppointment.getClinicianId());
      defaultValuesByKey.put("facilityId", selectedAppointment.getFacilityId());
      defaultValuesByKey.put("dateTime", selectedAppointment.getDateTime());
      defaultValuesByKey.put("status", selectedAppointment.getStatus() == null ? "" : selectedAppointment.getStatus().name());
      defaultValuesByKey.put("reason", selectedAppointment.getReason());

      FormDialog formDialog = new FormDialog(owner, "Edit Appointment", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Appointment updatedAppointment = new Appointment(
          selectedAppointment.getId(),
          valuesByKey.getOrDefault("patientId", ""),
          valuesByKey.getOrDefault("clinicianId", ""),
          valuesByKey.getOrDefault("facilityId", ""),
          valuesByKey.getOrDefault("dateTime", ""),
          parseAppointmentStatus(valuesByKey.getOrDefault("status", "")),
          valuesByKey.getOrDefault("reason", "")
      );

      appointmentController.updateAppointment(updatedAppointment);
      refreshAppointmentsTable();
      selectAppointmentById(updatedAppointment.getId());
      ActionFeedbackNotifier.showSuccess(this, "Appointment updated: " + safe(updatedAppointment.getId()));
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private AppointmentStatus parseAppointmentStatus(String rawStatus) {
    if (rawStatus == null) {
      return AppointmentStatus.SCHEDULED;
    }
    String normalized = rawStatus.trim().toUpperCase();
    if (normalized.isEmpty()) {
      return AppointmentStatus.SCHEDULED;
    }
    try {
      return AppointmentStatus.valueOf(normalized);
    } catch (IllegalArgumentException exception) {
      return AppointmentStatus.SCHEDULED;
    }
  }

  private List<SelectionItem> buildPatientSelectionItems() {
    List<Patient> patients = patientController.getAllPatients();
    List<SelectionItem> items = new java.util.ArrayList<>();
    for (Patient patient : patients) {
      if (patient == null) {
        continue;
      }
      String id = safe(patient.getId());
      if (id.isEmpty()) {
        continue;
      }
      String name = safe(patient.getFullName());
      String label = name.isEmpty() ? ("Patient ID: " + id) : (name + " (ID: " + id + ")");
      items.add(new SelectionItem(id, label));
    }
    return items;
  }

  private List<SelectionItem> buildClinicianSelectionItems() {
    List<Clinician> clinicians = clinicianController.getAllClinicians();
    List<SelectionItem> items = new java.util.ArrayList<>();
    for (Clinician clinician : clinicians) {
      if (clinician == null) {
        continue;
      }
      String id = safe(clinician.getId());
      if (id.isEmpty()) {
        continue;
      }
      String name = safe(clinician.getFullName());
      String label = name.isEmpty() ? ("Clinician ID: " + id) : (name + " (ID: " + id + ")");
      items.add(new SelectionItem(id, label));
    }
    return items;
  }

  private List<SelectionItem> buildFacilitySelectionItems() {
    List<Facility> facilities = facilityController.getAllFacilities();
    List<SelectionItem> items = new java.util.ArrayList<>();
    for (Facility facility : facilities) {
      if (facility == null) {
        continue;
      }
      String id = safe(facility.getId());
      if (id.isEmpty()) {
        continue;
      }
      String name = safe(facility.getName());
      String label = name.isEmpty() ? ("Facility ID: " + id) : (name + " (ID: " + id + ")");
      items.add(new SelectionItem(id, label));
    }
    return items;
  }

  private List<SelectionItem> buildAppointmentStatusSelectionItems() {
    List<SelectionItem> items = new java.util.ArrayList<>();
    for (AppointmentStatus status : AppointmentStatus.values()) {
      items.add(new SelectionItem(status.name(), status.name()));
    }
    return items;
  }

  private Map<String, String> buildPatientNamesByPatientId() {
    Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
    List<Patient> patients = patientController.getAllPatients();
    for (Patient patient : patients) {
      if (patient == null) {
        continue;
      }
      String id = safe(patient.getId());
      if (id.isEmpty()) {
        continue;
      }
      patientNamesByPatientId.put(id, safe(patient.getFullName()));
    }
    return patientNamesByPatientId;
  }

  private Map<String, String> buildClinicianNamesByClinicianId() {
    Map<String, String> clinicianNamesByClinicianId = new LinkedHashMap<>();
    List<Clinician> clinicians = clinicianController.getAllClinicians();
    for (Clinician clinician : clinicians) {
      if (clinician == null) {
        continue;
      }
      String id = safe(clinician.getId());
      if (id.isEmpty()) {
        continue;
      }
      clinicianNamesByClinicianId.put(id, safe(clinician.getFullName()));
    }
    return clinicianNamesByClinicianId;
  }

  private Map<String, String> buildFacilityNamesByFacilityId() {
    Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();
    List<Facility> facilities = facilityController.getAllFacilities();
    for (Facility facility : facilities) {
      if (facility == null) {
        continue;
      }
      String id = safe(facility.getId());
      if (id.isEmpty()) {
        continue;
      }
      facilityNamesByFacilityId.put(id, safe(facility.getName()));
    }
    return facilityNamesByFacilityId;
  }

  private void selectAppointmentById(String appointmentId) {
    if (appointmentId == null || appointmentId.trim().isEmpty()) {
      return;
    }

    for (int rowIndex = 0; rowIndex < appointmentsTableModel.getRowCount(); rowIndex++) {
      Appointment appointment = appointmentsTableModel.getAppointmentAtRow(rowIndex);
      if (appointment != null && appointmentId.equals(appointment.getId())) {
        appointmentsTable.setRowSelectionInterval(rowIndex, rowIndex);
        appointmentsTable.scrollRectToVisible(appointmentsTable.getCellRect(rowIndex, 0, true));
        return;
      }
    }
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}