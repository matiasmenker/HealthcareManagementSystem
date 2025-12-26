package hms.view.patients;

import hms.controller.PatientController;
import hms.model.Patient;
import hms.view.common.ButtonsActionsBar;
import hms.view.common.FormDialog;
import hms.view.common.FormFieldViewConfiguration;

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
  private final PatientsTableModel patientsTableModel;
  private final JTable patientsTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public PatientsPanel(PatientController patientController) {
    this.patientController = Objects.requireNonNull(patientController, "patientController");

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
      patientsTableModel.setPatients(patients);
      buttonsActionsBar.setEditEnabled(patientsTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddPatientDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("patientId", "Patient ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.optionalEditable("nhsNumber", "NHS Number"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.optionalEditable("address", "Address"),
          FormFieldViewConfiguration.optionalEditable("gpSurgeryId", "GP Surgery ID")
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
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("address", ""),
          valuesByKey.getOrDefault("gpSurgeryId", "")
      );

      patientController.addPatient(patient);
      refreshPatientsTable();
      selectPatientById(patient.getId());
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

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("patientId", "Patient ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.optionalEditable("nhsNumber", "NHS Number"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.optionalEditable("address", "Address"),
          FormFieldViewConfiguration.optionalEditable("gpSurgeryId", "GP Surgery ID")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("patientId", selectedPatient.getId());
      defaultValuesByKey.put("fullName", selectedPatient.getFullName());
      defaultValuesByKey.put("email", selectedPatient.getEmail());
      defaultValuesByKey.put("nhsNumber", selectedPatient.getNhsNumber());
      defaultValuesByKey.put("phoneNumber", selectedPatient.getPhone());
      defaultValuesByKey.put("address", selectedPatient.getAddress());
      defaultValuesByKey.put("gpSurgeryId", selectedPatient.getRegisteredFacilityId());

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
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("address", ""),
          valuesByKey.getOrDefault("gpSurgeryId", "")
      );

      patientController.updatePatient(updatedPatient);
      refreshPatientsTable();
      selectPatientById(updatedPatient.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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
}