package hms.view.referrals;

import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.controller.ReferralController;
import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;
import hms.model.enums.ReferralStatus;
import hms.model.singleton.ReferralManager;
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
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReferralsPanel extends JPanel {

  private final ReferralController referralController;
  private final PatientController patientController;
  private final ClinicianController clinicianController;
  private final FacilityController facilityController;

  private final ReferralsTableModel referralsTableModel;
  private final JTable referralsTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public ReferralsPanel(ReferralController referralController,
                        PatientController patientController,
                        ClinicianController clinicianController,
                        FacilityController facilityController) {
    this.referralController = Objects.requireNonNull(referralController, "referralController");
    this.patientController = Objects.requireNonNull(patientController, "patientController");
    this.clinicianController = Objects.requireNonNull(clinicianController, "clinicianController");
    this.facilityController = Objects.requireNonNull(facilityController, "facilityController");

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    referralsTableModel = new ReferralsTableModel();
    referralsTable = new JTable(referralsTableModel);
    referralsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
      @Override
      public void onAdd() {
        openAddReferralDialog();
      }

      @Override
      public void onEdit() {
        openEditReferralDialog();
      }

      @Override
      public void onRefresh() {
        refreshReferralsTable();
      }
    });

    buttonsActionsBar.setDeleteHandler(this::deleteSelectedReferral);
    buttonsActionsBar.setEditEnabled(false);
    buttonsActionsBar.setDeleteEnabled(false);

    referralsTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = referralsTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
      buttonsActionsBar.setDeleteEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(referralsTable), BorderLayout.CENTER);

    refreshReferralsTable();
  }

  private void refreshReferralsTable() {
    try {
      List<Referral> referrals = referralController.getAllReferrals();
      Map<String, String> patientNamesByPatientId = buildPatientNamesByPatientId();
      Map<String, String> clinicianNamesByClinicianId = buildClinicianNamesByClinicianId();
      Map<String, String> facilityNamesByFacilityId = buildFacilityNamesByFacilityId();
      referralsTableModel.setReferrals(referrals, patientNamesByPatientId, clinicianNamesByClinicianId, facilityNamesByFacilityId);

      boolean hasSelection = referralsTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
      buttonsActionsBar.setDeleteEnabled(hasSelection);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddReferralDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> patientOptions = buildPatientSelectionItems();
      List<SelectionItem> clinicianOptions = buildClinicianSelectionItems();
      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();
      List<SelectionItem> statusOptions = buildReferralStatusSelectionItems();

      String today = LocalDate.now().toString();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("referralId", "Referral ID"),
          FormFieldViewConfiguration.requiredSelect("patientId", "Patient", patientOptions),
          FormFieldViewConfiguration.requiredSelect("referringClinicianId", "Referring Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("referredClinicianId", "Referred Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("referringFacilityId", "Referring Facility", facilityOptions),
          FormFieldViewConfiguration.requiredSelect("referredFacilityId", "Referred Facility", facilityOptions),
          FormFieldViewConfiguration.requiredEditable("referralDate", "Referral Date (YYYY-MM-DD)"),
          FormFieldViewConfiguration.requiredEditable("urgencyLevel", "Urgency Level"),
          FormFieldViewConfiguration.requiredEditable("referralReason", "Referral Reason"),
          FormFieldViewConfiguration.requiredEditable("clinicalSummary", "Clinical Summary"),
          FormFieldViewConfiguration.requiredEditable("requestedInvestigations", "Requested Investigations"),
          FormFieldViewConfiguration.requiredSelect("status", "Status", statusOptions),
          FormFieldViewConfiguration.optionalEditable("appointmentId", "Appointment ID"),
          FormFieldViewConfiguration.optionalEditable("notes", "Notes")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("referralDate", today);
      defaultValuesByKey.put("status", ReferralStatus.CREATED.name());

      FormDialog formDialog = new FormDialog(owner, "Add Referral", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      String referralDate = safe(valuesByKey.getOrDefault("referralDate", ""));
      String createdDate = referralDate;
      String lastUpdated = referralDate;

      Referral referral = new Referral(
          valuesByKey.getOrDefault("referralId", ""),
          valuesByKey.getOrDefault("patientId", ""),
          valuesByKey.getOrDefault("referringClinicianId", ""),
          valuesByKey.getOrDefault("referredClinicianId", ""),
          valuesByKey.getOrDefault("referringFacilityId", ""),
          valuesByKey.getOrDefault("referredFacilityId", ""),
          referralDate,
          valuesByKey.getOrDefault("urgencyLevel", ""),
          valuesByKey.getOrDefault("referralReason", ""),
          valuesByKey.getOrDefault("clinicalSummary", ""),
          valuesByKey.getOrDefault("requestedInvestigations", ""),
          parseReferralStatus(valuesByKey.getOrDefault("status", "")),
          valuesByKey.getOrDefault("appointmentId", ""),
          valuesByKey.getOrDefault("notes", ""),
          createdDate,
          lastUpdated
      );

      ReferralManager.getInstance().createReferral(referral);

      refreshReferralsTable();
      selectReferralById(referral.getId());

      JOptionPane.showMessageDialog(
          this,
          "Referral created. Output files and logs have been generated in the output folder.",
          "Referral created",
          JOptionPane.INFORMATION_MESSAGE
      );
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openEditReferralDialog() {
    int selectedRowIndex = referralsTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select a referral to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Referral selectedReferral = referralsTableModel.getReferralAtRow(selectedRowIndex);
    if (selectedReferral == null) {
      JOptionPane.showMessageDialog(this, "Select a referral to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<SelectionItem> patientOptions = buildPatientSelectionItems();
      List<SelectionItem> clinicianOptions = buildClinicianSelectionItems();
      List<SelectionItem> facilityOptions = buildFacilitySelectionItems();
      List<SelectionItem> statusOptions = buildReferralStatusSelectionItems();

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("referralId", "Referral ID"),
          FormFieldViewConfiguration.requiredSelect("patientId", "Patient", patientOptions),
          FormFieldViewConfiguration.requiredSelect("referringClinicianId", "Referring Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("referredClinicianId", "Referred Clinician", clinicianOptions),
          FormFieldViewConfiguration.requiredSelect("referringFacilityId", "Referring Facility", facilityOptions),
          FormFieldViewConfiguration.requiredSelect("referredFacilityId", "Referred Facility", facilityOptions),
          FormFieldViewConfiguration.requiredEditable("referralDate", "Referral Date (YYYY-MM-DD)"),
          FormFieldViewConfiguration.requiredEditable("urgencyLevel", "Urgency Level"),
          FormFieldViewConfiguration.requiredEditable("referralReason", "Referral Reason"),
          FormFieldViewConfiguration.requiredEditable("clinicalSummary", "Clinical Summary"),
          FormFieldViewConfiguration.requiredEditable("requestedInvestigations", "Requested Investigations"),
          FormFieldViewConfiguration.requiredSelect("status", "Status", statusOptions),
          FormFieldViewConfiguration.optionalEditable("appointmentId", "Appointment ID"),
          FormFieldViewConfiguration.optionalEditable("notes", "Notes")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("referralId", selectedReferral.getId());
      defaultValuesByKey.put("patientId", selectedReferral.getPatientId());
      defaultValuesByKey.put("referringClinicianId", selectedReferral.getReferringClinicianId());
      defaultValuesByKey.put("referredClinicianId", selectedReferral.getReferredToClinicianId());
      defaultValuesByKey.put("referringFacilityId", selectedReferral.getReferringFacilityId());
      defaultValuesByKey.put("referredFacilityId", selectedReferral.getReferredToFacilityId());
      defaultValuesByKey.put("referralDate", selectedReferral.getReferralDate());
      defaultValuesByKey.put("urgencyLevel", selectedReferral.getUrgencyLevel());
      defaultValuesByKey.put("referralReason", selectedReferral.getReferralReason());
      defaultValuesByKey.put("clinicalSummary", selectedReferral.getClinicalSummary());
      defaultValuesByKey.put("requestedInvestigations", selectedReferral.getRequestedInvestigations());
      defaultValuesByKey.put("status", selectedReferral.getStatus() == null ? "" : selectedReferral.getStatus().name());
      defaultValuesByKey.put("appointmentId", selectedReferral.getAppointmentId());
      defaultValuesByKey.put("notes", selectedReferral.getNotes());

      FormDialog formDialog = new FormDialog(owner, "Edit Referral", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      String today = LocalDate.now().toString();

      Referral updatedReferral = new Referral(
          selectedReferral.getId(),
          valuesByKey.getOrDefault("patientId", ""),
          valuesByKey.getOrDefault("referringClinicianId", ""),
          valuesByKey.getOrDefault("referredClinicianId", ""),
          valuesByKey.getOrDefault("referringFacilityId", ""),
          valuesByKey.getOrDefault("referredFacilityId", ""),
          valuesByKey.getOrDefault("referralDate", ""),
          valuesByKey.getOrDefault("urgencyLevel", ""),
          valuesByKey.getOrDefault("referralReason", ""),
          valuesByKey.getOrDefault("clinicalSummary", ""),
          valuesByKey.getOrDefault("requestedInvestigations", ""),
          parseReferralStatus(valuesByKey.getOrDefault("status", "")),
          valuesByKey.getOrDefault("appointmentId", ""),
          valuesByKey.getOrDefault("notes", ""),
          selectedReferral.getCreatedDate(),
          today
      );

      referralController.updateReferral(updatedReferral);

      refreshReferralsTable();
      selectReferralById(updatedReferral.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelectedReferral() {
    int selectedRowIndex = referralsTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select a referral to delete.", "Delete", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Referral selectedReferral = referralsTableModel.getReferralAtRow(selectedRowIndex);
    if (selectedReferral == null) {
      JOptionPane.showMessageDialog(this, "Select a referral to delete.", "Delete", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String referralId = safe(selectedReferral.getId());
    if (referralId.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Selected referral has no identifier.", "Delete", JOptionPane.ERROR_MESSAGE);
      return;
    }

    int choice = JOptionPane.showConfirmDialog(
        this,
        "Delete referral " + referralId + "?",
        "Confirm delete",
        JOptionPane.YES_NO_OPTION
    );

    if (choice != JOptionPane.YES_OPTION) {
      return;
    }

    try {
      referralController.deleteReferral(referralId);
      refreshReferralsTable();
      JOptionPane.showMessageDialog(this, "Referral deleted: " + referralId, "Deleted", JOptionPane.INFORMATION_MESSAGE);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private ReferralStatus parseReferralStatus(String rawStatus) {
    if (rawStatus == null) {
      return ReferralStatus.CREATED;
    }
    String normalized = rawStatus.trim().toUpperCase();
    if (normalized.isEmpty()) {
      return ReferralStatus.CREATED;
    }
    try {
      return ReferralStatus.valueOf(normalized);
    } catch (IllegalArgumentException exception) {
      return ReferralStatus.CREATED;
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
      String label = name.isEmpty() ? ("Patient " + id) : (name + " (ID: " + id + ")");
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
      String label = name.isEmpty() ? ("Clinician " + id) : (name + " (ID: " + id + ")");
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
      String label = name.isEmpty() ? ("Facility " + id) : (name + " (ID: " + id + ")");
      items.add(new SelectionItem(id, label));
    }
    return items;
  }

  private List<SelectionItem> buildReferralStatusSelectionItems() {
    List<SelectionItem> items = new java.util.ArrayList<>();
    for (ReferralStatus status : ReferralStatus.values()) {
      items.add(new SelectionItem(status.name(), status.name()));
    }
    return items;
  }

  private Map<String, String> buildPatientNamesByPatientId() {
    Map<String, String> namesById = new LinkedHashMap<>();
    List<Patient> patients = patientController.getAllPatients();
    for (Patient patient : patients) {
      if (patient == null) {
        continue;
      }
      String id = safe(patient.getId());
      if (id.isEmpty()) {
        continue;
      }
      namesById.put(id, safe(patient.getFullName()));
    }
    return namesById;
  }

  private Map<String, String> buildClinicianNamesByClinicianId() {
    Map<String, String> namesById = new LinkedHashMap<>();
    List<Clinician> clinicians = clinicianController.getAllClinicians();
    for (Clinician clinician : clinicians) {
      if (clinician == null) {
        continue;
      }
      String id = safe(clinician.getId());
      if (id.isEmpty()) {
        continue;
      }
      namesById.put(id, safe(clinician.getFullName()));
    }
    return namesById;
  }

  private Map<String, String> buildFacilityNamesByFacilityId() {
    Map<String, String> namesById = new LinkedHashMap<>();
    List<Facility> facilities = facilityController.getAllFacilities();
    for (Facility facility : facilities) {
      if (facility == null) {
        continue;
      }
      String id = safe(facility.getId());
      if (id.isEmpty()) {
        continue;
      }
      namesById.put(id, safe(facility.getName()));
    }
    return namesById;
  }

  private void selectReferralById(String referralId) {
    if (referralId == null || referralId.trim().isEmpty()) {
      return;
    }

    for (int rowIndex = 0; rowIndex < referralsTableModel.getRowCount(); rowIndex++) {
      Referral referral = referralsTableModel.getReferralAtRow(rowIndex);
      if (referral != null && referralId.equals(referral.getId())) {
        referralsTable.setRowSelectionInterval(rowIndex, rowIndex);
        referralsTable.scrollRectToVisible(referralsTable.getCellRect(rowIndex, 0, true));
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