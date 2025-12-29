package hms.view.facilities;

import hms.controller.FacilityController;
import hms.model.Facility;
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

public class FacilitiesPanel extends JPanel {

	private final FacilityController facilityController;
	private final FacilitiesTableModel facilitiesTableModel;
	private final JTable facilitiesTable;
	private final ButtonsActionsBar buttonsActionsBar;

	public FacilitiesPanel(FacilityController facilityController) {
		this.facilityController = Objects.requireNonNull(facilityController, "facilityController");

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		facilitiesTableModel = new FacilitiesTableModel();
		facilitiesTable = new JTable(facilitiesTableModel);
		facilitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
			@Override
			public void onAdd() {
				openAddFacilityDialog();
			}

			@Override
			public void onEdit() {
				openEditFacilityDialog();
			}

			@Override
			public void onRefresh() {
				refreshFacilitiesTable();
			}
		});

		buttonsActionsBar.setEditEnabled(false);

		facilitiesTable.getSelectionModel().addListSelectionListener(event -> {
			if (event.getValueIsAdjusting()) {
				return;
			}
			boolean hasSelection = facilitiesTable.getSelectedRow() >= 0;
			buttonsActionsBar.setEditEnabled(hasSelection);
		});

		add(buttonsActionsBar, BorderLayout.NORTH);
		add(new JScrollPane(facilitiesTable), BorderLayout.CENTER);

		refreshFacilitiesTable();
	}

	private void refreshFacilitiesTable() {
		try {
			List<Facility> facilities = facilityController.getAllFacilities();
			facilitiesTableModel.setFacilities(facilities);
			buttonsActionsBar.setEditEnabled(facilitiesTable.getSelectedRow() >= 0);
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openAddFacilityDialog() {
		try {
			Window owner = SwingUtilities.getWindowAncestor(this);

			List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
					FormFieldViewConfiguration.requiredEditable("facilityId", "Facility ID"),
					FormFieldViewConfiguration.requiredEditable("facilityName", "Name"),
					FormFieldViewConfiguration.requiredEditable("facilityType", "Type"),
					FormFieldViewConfiguration.requiredEditable("address", "Address"),
					FormFieldViewConfiguration.optionalEditable("postcode", "Postcode"),
					FormFieldViewConfiguration.requiredEditable("phoneNumber", "Phone Number"),
					FormFieldViewConfiguration.optionalEditable("email", "Email"),
					FormFieldViewConfiguration.optionalEditable("openingHours", "Opening Hours"),
					FormFieldViewConfiguration.optionalEditable("managerName", "Manager Name"),
					FormFieldViewConfiguration.requiredEditable("capacity", "Capacity"),
					FormFieldViewConfiguration.optionalEditable("specialitiesOffered", "Specialities Offered"));

			FormDialog formDialog = new FormDialog(owner, "Add Facility", fieldViewConfigurations, Map.of());
			formDialog.setVisible(true);

			if (!formDialog.isConfirmed()) {
				return;
			}

			Map<String, String> valuesByKey = formDialog.getValuesByKey();

			Facility facility = new Facility(valuesByKey.getOrDefault("facilityId", ""),
					valuesByKey.getOrDefault("facilityName", ""), valuesByKey.getOrDefault("facilityType", ""),
					valuesByKey.getOrDefault("address", ""), valuesByKey.getOrDefault("postcode", ""),
					valuesByKey.getOrDefault("phoneNumber", ""), valuesByKey.getOrDefault("email", ""),
					valuesByKey.getOrDefault("openingHours", ""), valuesByKey.getOrDefault("managerName", ""),
					valuesByKey.getOrDefault("capacity", ""), valuesByKey.getOrDefault("specialitiesOffered", ""));

			facilityController.addFacility(facility);
			refreshFacilitiesTable();
			selectFacilityById(facility.getId());
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openEditFacilityDialog() {
		int selectedRowIndex = facilitiesTable.getSelectedRow();
		if (selectedRowIndex < 0) {
			JOptionPane.showMessageDialog(this, "Select a facility to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		Facility selectedFacility = facilitiesTableModel.getFacilityAtRow(selectedRowIndex);
		if (selectedFacility == null) {
			JOptionPane.showMessageDialog(this, "Select a facility to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			Window owner = SwingUtilities.getWindowAncestor(this);

			List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
					FormFieldViewConfiguration.requiredReadOnly("facilityId", "Facility ID"),
					FormFieldViewConfiguration.requiredEditable("facilityName", "Name"),
					FormFieldViewConfiguration.requiredEditable("facilityType", "Type"),
					FormFieldViewConfiguration.requiredEditable("address", "Address"),
					FormFieldViewConfiguration.optionalEditable("postcode", "Postcode"),
					FormFieldViewConfiguration.requiredEditable("phoneNumber", "Phone Number"),
					FormFieldViewConfiguration.optionalEditable("email", "Email"),
					FormFieldViewConfiguration.optionalEditable("openingHours", "Opening Hours"),
					FormFieldViewConfiguration.optionalEditable("managerName", "Manager Name"),
					FormFieldViewConfiguration.requiredEditable("capacity", "Capacity"),
					FormFieldViewConfiguration.optionalEditable("specialitiesOffered", "Specialities Offered"));

			Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
			defaultValuesByKey.put("facilityId", selectedFacility.getId());
			defaultValuesByKey.put("facilityName", selectedFacility.getName());
			defaultValuesByKey.put("facilityType", selectedFacility.getType());
			defaultValuesByKey.put("address", selectedFacility.getAddress());
			defaultValuesByKey.put("postcode", selectedFacility.getPostcode());
			defaultValuesByKey.put("phoneNumber", selectedFacility.getPhoneNumber());
			defaultValuesByKey.put("email", selectedFacility.getEmail());
			defaultValuesByKey.put("openingHours", selectedFacility.getOpeningHours());
			defaultValuesByKey.put("managerName", selectedFacility.getManagerName());
			defaultValuesByKey.put("capacity", selectedFacility.getCapacity());
			defaultValuesByKey.put("specialitiesOffered", selectedFacility.getSpecialitiesOffered());

			FormDialog formDialog = new FormDialog(owner, "Edit Facility", fieldViewConfigurations, defaultValuesByKey);
			formDialog.setVisible(true);

			if (!formDialog.isConfirmed()) {
				return;
			}

			Map<String, String> valuesByKey = formDialog.getValuesByKey();

			Facility updatedFacility = new Facility(selectedFacility.getId(),
					valuesByKey.getOrDefault("facilityName", ""), valuesByKey.getOrDefault("facilityType", ""),
					valuesByKey.getOrDefault("address", ""), valuesByKey.getOrDefault("postcode", ""),
					valuesByKey.getOrDefault("phoneNumber", ""), valuesByKey.getOrDefault("email", ""),
					valuesByKey.getOrDefault("openingHours", ""), valuesByKey.getOrDefault("managerName", ""),
					valuesByKey.getOrDefault("capacity", ""), valuesByKey.getOrDefault("specialitiesOffered", ""));

			facilityController.updateFacility(updatedFacility);
			refreshFacilitiesTable();
			selectFacilityById(updatedFacility.getId());
		} catch (RuntimeException exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void selectFacilityById(String facilityId) {
		if (facilityId == null || facilityId.trim().isEmpty()) {
			return;
		}

		for (int rowIndex = 0; rowIndex < facilitiesTableModel.getRowCount(); rowIndex++) {
			Facility facility = facilitiesTableModel.getFacilityAtRow(rowIndex);
			if (facility != null && facilityId.equals(facility.getId())) {
				facilitiesTable.setRowSelectionInterval(rowIndex, rowIndex);
				facilitiesTable.scrollRectToVisible(facilitiesTable.getCellRect(rowIndex, 0, true));
				return;
			}
		}
	}
}