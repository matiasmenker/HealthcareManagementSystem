package hms.view.patients;

import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.model.Facility;
import hms.model.Patient;
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

public class PatientsPanel extends JPanel {

  private final PatientController patientController;
  private final FacilityController facilityController;

  private final PatientsTableModel patientsTableModel;
  private final JTable patientsTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public PatientsPanel(PatientController patientController, FacilityController facilityController) {
    this.patientController = Objects.requireNonNull(patientController, "patientController");
    this.facilityController = Objects.requireNonNull(facilityController, "facilityController");

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    patientsTableModel = new PatientsTableModel();
    patientsTable = new JTable(patientsTableModel);
    patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
      @Override
      public void onAdd() {
        openAddPatientDialog();
      }

      @Override
      public void onEdit() {
        openEditPatientDialog();
      }

      @Override
      public void onRefresh() {
        refreshPatientsTable();
      }
    });

    buttonsActionsBar.setEditEnabled(false);

    patientsTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = patientsTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(patientsTable), BorderLayout.CENTER);

    refreshPatientsTable();
  }

  private void refreshPatientsTable() {
    try {
      List<Patient> patients = patientController.getAllPatients();
      Map<String, String> facilityNamesByFacilityId = buildFacilityNamesByFacilityId();
      patientsTableModel.setPatients(patients, facilityNamesByFacilityId);
      buttonsActionsBar.setEditEnabled(patientsTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddPatientDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("patientId", "Patient ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.requiredEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("nhsNumber", "NHS Number"),
          FormFieldViewConfiguration.requiredEditable("phone", "Phone"),
          FormFieldViewConfiguration.requiredSelect("registeredFacilityId", "Registered Facility", facilityOptions)
      );

      FormDialog formDialog = new FormDialog(owner, "Add Patient", fieldViewConfigurations, Map.of());
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Patient patient = new Patient(
          valuesByKey.getOrDefault("patientId", ""),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("nhsNumber", ""),
          valuesByKey.getOrDefault("phone", ""),
          "",
          valuesByKey.getOrDefault("registeredFacilityId", "")
      );

      patientController.addPatient(patient);
      refreshPatientsTable();
      selectPatientById(patient.getId());
      ActionFeedbackNotifier.showSuccess(this, "Patient created: " + safe(patient.getId()));
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openEditPatientDialog() {
    int selectedRowIndex = patientsTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select a patient to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Patient selectedPatient = patientsTableModel.getPatientAtRow(selectedRowIndex);
    if (selectedPatient == null) {
      JOptionPane.showMessageDialog(this, "Select a patient to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("patientId", "Patient ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.requiredEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("nhsNumber", "NHS Number"),
          FormFieldViewConfiguration.requiredEditable("phone", "Phone"),
          FormFieldViewConfiguration.requiredSelect("registeredFacilityId", "Registered Facility", facilityOptions)
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("patientId", safe(selectedPatient.getId()));
      defaultValuesByKey.put("fullName", safe(selectedPatient.getFullName()));
      defaultValuesByKey.put("email", safe(selectedPatient.getEmail()));
      defaultValuesByKey.put("nhsNumber", safe(selectedPatient.getNhsNumber()));
      defaultValuesByKey.put("phone", safe(selectedPatient.getPhone()));
      defaultValuesByKey.put("registeredFacilityId", safe(selectedPatient.getRegisteredFacilityId()));

      FormDialog formDialog = new FormDialog(owner, "Edit Patient", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Patient updatedPatient = new Patient(
          selectedPatient.getId(),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("nhsNumber", ""),
          valuesByKey.getOrDefault("phone", ""),
          safe(selectedPatient.getAddress()),
          valuesByKey.getOrDefault("registeredFacilityId", "")
      );

      patientController.updatePatient(updatedPatient);
      refreshPatientsTable();
      selectPatientById(updatedPatient.getId());
      ActionFeedbackNotifier.showSuccess(this, "Patient updated: " + safe(updatedPatient.getId()));
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private Map<String, String> buildFacilityNamesByFacilityId() {
    Map<String, String> facilityNamesByFacilityId = new LinkedHashMap<>();
    List<Facility> facilities = facilityController.getAllFacilities();
    for (Facility facility : facilities) {
      if (facility == null) {
        continue;
      }
      String facilityId = safe(facility.getId());
      if (facilityId.isEmpty()) {
        continue;
      }
      facilityNamesByFacilityId.put(facilityId, safe(facility.getName()));
    }
    return facilityNamesByFacilityId;
  }

  private List<SelectionItem> buildFacilitySelectionItems() {
    List<SelectionItem> items = new java.util.ArrayList<>();
    List<Facility> facilities = facilityController.getAllFacilities();
    for (Facility facility : facilities) {
      if (facility == null) {
        continue;
      }
      String id = safe(facility.getId());
      if (id.isEmpty()) {
        continue;
      }
      String name = safe(facility.getName());
      String label = name.isEmpty() ? id : (name + " (" + id + ")");
      items.add(new SelectionItem(id, label));
    }
    return items;
  }

  private void selectPatientById(String patientId) {
    if (patientId == null || patientId.trim().isEmpty()) {
      return;
    }

    for (int rowIndex = 0; rowIndex < patientsTableModel.getRowCount(); rowIndex++) {
      Patient patient = patientsTableModel.getPatientAtRow(rowIndex);
      if (patient != null && patientId.equals(patient.getId())) {
        patientsTable.setRowSelectionInterval(rowIndex, rowIndex);
        patientsTable.scrollRectToVisible(patientsTable.getCellRect(rowIndex, 0, true));
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