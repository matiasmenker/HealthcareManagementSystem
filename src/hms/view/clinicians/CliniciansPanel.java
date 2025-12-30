package hms.view.clinicians;

import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.model.Clinician;
import hms.model.Facility;
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

public class CliniciansPanel extends JPanel {

  private final ClinicianController clinicianController;
  private final FacilityController facilityController;

  private final CliniciansTableModel cliniciansTableModel;
  private final JTable cliniciansTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public CliniciansPanel(ClinicianController clinicianController, FacilityController facilityController) {
    this.clinicianController = Objects.requireNonNull(clinicianController, "clinicianController");
    this.facilityController = Objects.requireNonNull(facilityController, "facilityController");

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
      Map<String, String> facilityNamesByFacilityId = buildFacilityNamesByFacilityId();
      cliniciansTableModel.setClinicians(clinicians, facilityNamesByFacilityId);
      buttonsActionsBar.setEditEnabled(cliniciansTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddClinicianDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("clinicianId", "Clinician ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.requiredEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("role", "Role"),
          FormFieldViewConfiguration.requiredEditable("qualification", "Qualification"),
          FormFieldViewConfiguration.requiredSelect("facilityId", "Facility", facilityOptions)
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
          valuesByKey.getOrDefault("role", ""),
          valuesByKey.getOrDefault("qualification", ""),
          valuesByKey.getOrDefault("facilityId", "")
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

      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("clinicianId", "Clinician ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.requiredEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("role", "Role"),
          FormFieldViewConfiguration.requiredEditable("qualification", "Qualification"),
          FormFieldViewConfiguration.requiredSelect("facilityId", "Facility", facilityOptions)
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("clinicianId", selectedClinician.getId());
      defaultValuesByKey.put("fullName", selectedClinician.getFullName());
      defaultValuesByKey.put("email", selectedClinician.getEmail());
      defaultValuesByKey.put("role", selectedClinician.getRole());
      defaultValuesByKey.put("qualification", selectedClinician.getQualification());
      defaultValuesByKey.put("facilityId", selectedClinician.getFacilityId());

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
          valuesByKey.getOrDefault("role", ""),
          valuesByKey.getOrDefault("qualification", ""),
          valuesByKey.getOrDefault("facilityId", "")
      );

      clinicianController.updateClinician(updatedClinician);
      refreshCliniciansTable();
      selectClinicianById(updatedClinician.getId());
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
      String id = safe(facility.getId());
      if (id.isEmpty()) {
        continue;
      }
      facilityNamesByFacilityId.put(id, safe(facility.getName()));
    }
    return facilityNamesByFacilityId;
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

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}