package hms.view.clinicians;

import hms.controller.ClinicianController;
import hms.model.Clinician;
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

public class CliniciansPanel extends JPanel {

  private final ClinicianController clinicianController;
  private final CliniciansTableModel cliniciansTableModel;
  private final JTable cliniciansTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public CliniciansPanel(ClinicianController clinicianController) {
    this.clinicianController = Objects.requireNonNull(clinicianController, "clinicianController");

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    cliniciansTableModel = new CliniciansTableModel();
    cliniciansTable = new JTable(cliniciansTableModel);
    cliniciansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
      @Override
      public void onAdd() {
        openAddClinicianDialog();
      }

      @Override
      public void onEdit() {
        openEditClinicianDialog();
      }

      @Override
      public void onRefresh() {
        refreshCliniciansTable();
      }
    });

    buttonsActionsBar.setEditEnabled(false);

    cliniciansTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = cliniciansTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(cliniciansTable), BorderLayout.CENTER);

    refreshCliniciansTable();
  }

  private void refreshCliniciansTable() {
    try {
      List<Clinician> clinicians = clinicianController.getAllClinicians();
      cliniciansTableModel.setClinicians(clinicians);
      buttonsActionsBar.setEditEnabled(cliniciansTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddClinicianDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("clinicianId", "Clinician ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("title", "Title"),
          FormFieldViewConfiguration.optionalEditable("specialty", "Specialty"),
          FormFieldViewConfiguration.optionalEditable("medicalRegistrationNumber", "Registration Number"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.requiredEditable("workplaceId", "Workplace ID"),
          FormFieldViewConfiguration.requiredEditable("workplaceType", "Workplace Type"),
          FormFieldViewConfiguration.optionalEditable("employmentStatus", "Employment Status"),
          FormFieldViewConfiguration.optionalEditable("startDate", "Start Date (YYYY-MM-DD)")
      );

      FormDialog formDialog = new FormDialog(owner, "Add Clinician", fieldViewConfigurations, Map.of());
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Clinician clinician = new Clinician(
          valuesByKey.getOrDefault("clinicianId", ""),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("title", ""),
          valuesByKey.getOrDefault("specialty", ""),
          valuesByKey.getOrDefault("medicalRegistrationNumber", ""),
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("workplaceId", ""),
          valuesByKey.getOrDefault("workplaceType", ""),
          valuesByKey.getOrDefault("employmentStatus", ""),
          valuesByKey.getOrDefault("startDate", "")
      );

      clinicianController.addClinician(clinician);
      refreshCliniciansTable();
      selectClinicianById(clinician.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openEditClinicianDialog() {
    int selectedRowIndex = cliniciansTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select a clinician to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Clinician selectedClinician = cliniciansTableModel.getClinicianAtRow(selectedRowIndex);
    if (selectedClinician == null) {
      JOptionPane.showMessageDialog(this, "Select a clinician to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("clinicianId", "Clinician ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("title", "Title"),
          FormFieldViewConfiguration.optionalEditable("specialty", "Specialty"),
          FormFieldViewConfiguration.optionalEditable("medicalRegistrationNumber", "Registration Number"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.requiredEditable("workplaceId", "Workplace ID"),
          FormFieldViewConfiguration.requiredEditable("workplaceType", "Workplace Type"),
          FormFieldViewConfiguration.optionalEditable("employmentStatus", "Employment Status"),
          FormFieldViewConfiguration.optionalEditable("startDate", "Start Date (YYYY-MM-DD)")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("clinicianId", selectedClinician.getId());
      defaultValuesByKey.put("fullName", selectedClinician.getFullName());
      defaultValuesByKey.put("email", selectedClinician.getEmail());
      defaultValuesByKey.put("title", selectedClinician.getTitle());
      defaultValuesByKey.put("specialty", selectedClinician.getSpecialty());
      defaultValuesByKey.put("medicalRegistrationNumber", selectedClinician.getMedicalRegistrationNumber());
      defaultValuesByKey.put("phoneNumber", selectedClinician.getPhoneNumber());
      defaultValuesByKey.put("workplaceId", selectedClinician.getWorkplaceId());
      defaultValuesByKey.put("workplaceType", selectedClinician.getWorkplaceType());
      defaultValuesByKey.put("employmentStatus", selectedClinician.getEmploymentStatus());
      defaultValuesByKey.put("startDate", selectedClinician.getStartDate());

      FormDialog formDialog = new FormDialog(owner, "Edit Clinician", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Clinician updatedClinician = new Clinician(
          selectedClinician.getId(),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("title", ""),
          valuesByKey.getOrDefault("specialty", ""),
          valuesByKey.getOrDefault("medicalRegistrationNumber", ""),
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("workplaceId", ""),
          valuesByKey.getOrDefault("workplaceType", ""),
          valuesByKey.getOrDefault("employmentStatus", ""),
          valuesByKey.getOrDefault("startDate", "")
      );

      clinicianController.updateClinician(updatedClinician);
      refreshCliniciansTable();
      selectClinicianById(updatedClinician.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void selectClinicianById(String clinicianId) {
    if (clinicianId == null || clinicianId.trim().isEmpty()) {
      return;
    }

    for (int rowIndex = 0; rowIndex < cliniciansTableModel.getRowCount(); rowIndex++) {
      Clinician clinician = cliniciansTableModel.getClinicianAtRow(rowIndex);
      if (clinician != null && clinicianId.equals(clinician.getId())) {
        cliniciansTable.setRowSelectionInterval(rowIndex, rowIndex);
        cliniciansTable.scrollRectToVisible(cliniciansTable.getCellRect(rowIndex, 0, true));
        return;
      }
    }
  }
}