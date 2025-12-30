package hms.view.prescriptions;

import hms.controller.ClinicianController;
import hms.controller.PatientController;
import hms.controller.PrescriptionController;
import hms.model.Clinician;
import hms.model.Patient;
import hms.model.Prescription;
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

public class PrescriptionsPanel extends JPanel {

	private final PrescriptionController prescriptionController;
	private final PatientController patientController;
	private final ClinicianController clinicianController;

	private final PrescriptionsTableModel prescriptionsTableModel;
	private final JTable prescriptionsTable;
	private final ButtonsActionsBar buttonsActionsBar;

	public PrescriptionsPanel(PrescriptionController prescriptionController, PatientController patientController,
			ClinicianController clinicianController) {
		this.prescriptionController = Objects.requireNonNull(prescriptionController, "prescriptionController");
		this.patientController = Objects.requireNonNull(patientController, "patientController");
		this.clinicianController = Objects.requireNonNull(clinicianController, "clinicianController");

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		prescriptionsTableModel = new PrescriptionsTableModel();
		prescriptionsTable = new JTable(prescriptionsTableModel);
		prescriptionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
			@Override
			public void onAdd() {
				openAddPrescriptionDialog();
			}

			@Override
			public void onEdit() {
				openEditPrescriptionDialog();
			}

			@Override
			public void onRefresh() {
				refreshPrescriptionsTable();
			}
		});

		buttonsActionsBar.setEditEnabled(false);

		prescriptionsTable.getSelectionModel().addListSelectionListener(event -> {
			if (event.getValueIsAdjusting()) {
				return;
			}
			boolean hasSelection = prescriptionsTable.getSelectedRow() >= 0;
			buttonsActionsBar.setEditEnabled(hasSelection);
		});

		add(buttonsActionsBar, BorderLayout.NORTH);
		add(new JScrollPane(prescriptionsTable), BorderLayout.CENTER);

		refreshPrescriptionsTable();
	}

	private void refreshPrescriptionsTable() {
		try {
			List<Prescription> prescriptions = prescriptionController.getAllPrescriptions();
			Map<String, String> patientNamesByPatientId = buildPatientNamesByPatientId();
			Map<String, String> clinicianNamesByClinicianId = buildClinicianNamesByClinicianId();
			prescriptionsTableModel.setPrescriptions(prescriptions, patientNamesByPatientId,
					clinicianNamesByClinicianId);
			buttonsActionsBar.setEditEnabled(prescriptionsTable.getSelectedRow() >= 0);
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Map<String, String> buildPatientNamesByPatientId() {
		Map<String, String> patientNamesByPatientId = new LinkedHashMap<>();
		List<Patient> patients = patientController.getAllPatients();
		for (Patient patient : patients) {
			if (patient == null) {
				continue;
			}
			String patientId = patient.getId();
			if (patientId == null || patientId.trim().isEmpty()) {
				continue;
			}
			patientNamesByPatientId.put(patientId, patient.getFullName() == null ? "" : patient.getFullName().trim());
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
			String clinicianId = clinician.getId();
			if (clinicianId == null || clinicianId.trim().isEmpty()) {
				continue;
			}
			clinicianNamesByClinicianId.put(clinicianId,
					clinician.getFullName() == null ? "" : clinician.getFullName().trim());
		}
		return clinicianNamesByClinicianId;
	}

	private void openAddPrescriptionDialog() {
		try {
			Window owner = SwingUtilities.getWindowAncestor(this);

			List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
					FormFieldViewConfiguration.requiredEditable("prescriptionId", "Prescription ID"),
					FormFieldViewConfiguration.requiredEditable("patientId", "Patient ID"),
					FormFieldViewConfiguration.requiredEditable("clinicianId", "Clinician ID"),
					FormFieldViewConfiguration.requiredEditable("medication", "Medication"),
					FormFieldViewConfiguration.requiredEditable("dosage", "Dosage"),
					FormFieldViewConfiguration.requiredEditable("pharmacy", "Pharmacy"),
					FormFieldViewConfiguration.requiredEditable("collectionStatus", "Status (Issued or Collected)"),
					FormFieldViewConfiguration.requiredEditable("dateIssued", "Date Issued (YYYY-MM-DD)"));

			FormDialog formDialog = new FormDialog(owner, "Add Prescription", fieldViewConfigurations, Map.of());
			formDialog.setVisible(true);

			if (!formDialog.isConfirmed()) {
				return;
			}

			Map<String, String> valuesByKey = formDialog.getValuesByKey();

			Prescription prescription = new Prescription(valuesByKey.getOrDefault("prescriptionId", ""),
					valuesByKey.getOrDefault("patientId", ""), valuesByKey.getOrDefault("clinicianId", ""),
					valuesByKey.getOrDefault("medication", ""), valuesByKey.getOrDefault("dosage", ""),
					valuesByKey.getOrDefault("pharmacy", ""), valuesByKey.getOrDefault("collectionStatus", ""),
					valuesByKey.getOrDefault("dateIssued", ""));

			prescriptionController.addPrescription(prescription);
			refreshPrescriptionsTable();
			selectPrescriptionById(prescription.getId());
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openEditPrescriptionDialog() {
		int selectedRowIndex = prescriptionsTable.getSelectedRow();
		if (selectedRowIndex < 0) {
			JOptionPane.showMessageDialog(this, "Select a prescription to edit.", "Edit",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		Prescription selectedPrescription = prescriptionsTableModel.getPrescriptionAtRow(selectedRowIndex);
		if (selectedPrescription == null) {
			JOptionPane.showMessageDialog(this, "Select a prescription to edit.", "Edit",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			Window owner = SwingUtilities.getWindowAncestor(this);

			List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
					FormFieldViewConfiguration.requiredReadOnly("prescriptionId", "Prescription ID"),
					FormFieldViewConfiguration.requiredEditable("patientId", "Patient ID"),
					FormFieldViewConfiguration.requiredEditable("clinicianId", "Clinician ID"),
					FormFieldViewConfiguration.requiredEditable("medication", "Medication"),
					FormFieldViewConfiguration.requiredEditable("dosage", "Dosage"),
					FormFieldViewConfiguration.requiredEditable("pharmacy", "Pharmacy"),
					FormFieldViewConfiguration.requiredEditable("collectionStatus", "Status (Issued or Collected)"),
					FormFieldViewConfiguration.requiredEditable("dateIssued", "Date Issued (YYYY-MM-DD)"));

			Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
			defaultValuesByKey.put("prescriptionId", selectedPrescription.getId());
			defaultValuesByKey.put("patientId", selectedPrescription.getPatientId());
			defaultValuesByKey.put("clinicianId", selectedPrescription.getClinicianId());
			defaultValuesByKey.put("medication", selectedPrescription.getMedication());
			defaultValuesByKey.put("dosage", selectedPrescription.getDosage());
			defaultValuesByKey.put("pharmacy", selectedPrescription.getPharmacy());
			defaultValuesByKey.put("collectionStatus", selectedPrescription.getCollectionStatus());
			defaultValuesByKey.put("dateIssued", selectedPrescription.getDateIssued());

			FormDialog formDialog = new FormDialog(owner, "Edit Prescription", fieldViewConfigurations,
					defaultValuesByKey);
			formDialog.setVisible(true);

			if (!formDialog.isConfirmed()) {
				return;
			}

			Map<String, String> valuesByKey = formDialog.getValuesByKey();

			Prescription updatedPrescription = new Prescription(selectedPrescription.getId(),
					valuesByKey.getOrDefault("patientId", ""), valuesByKey.getOrDefault("clinicianId", ""),
					valuesByKey.getOrDefault("medication", ""), valuesByKey.getOrDefault("dosage", ""),
					valuesByKey.getOrDefault("pharmacy", ""), valuesByKey.getOrDefault("collectionStatus", ""),
					valuesByKey.getOrDefault("dateIssued", ""));

			prescriptionController.updatePrescription(updatedPrescription);
			refreshPrescriptionsTable();
			selectPrescriptionById(updatedPrescription.getId());
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void selectPrescriptionById(String prescriptionId) {
		if (prescriptionId == null || prescriptionId.trim().isEmpty()) {
			return;
		}

		for (int rowIndex = 0; rowIndex < prescriptionsTableModel.getRowCount(); rowIndex++) {
			Prescription prescription = prescriptionsTableModel.getPrescriptionAtRow(rowIndex);
			if (prescription != null && prescriptionId.equals(prescription.getId())) {
				prescriptionsTable.setRowSelectionInterval(rowIndex, rowIndex);
				prescriptionsTable.scrollRectToVisible(prescriptionsTable.getCellRect(rowIndex, 0, true));
				return;
			}
		}
	}
}