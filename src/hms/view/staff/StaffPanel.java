package hms.view.staff;

import hms.controller.StaffController;
import hms.model.Staff;
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

public class StaffPanel extends JPanel {

  private final StaffController staffController;
  private final StaffTableModel staffTableModel;
  private final JTable staffTable;
  private final ButtonsActionsBar buttonsActionsBar;

  public StaffPanel(StaffController staffController) {
    this.staffController = Objects.requireNonNull(staffController, "staffController");

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    staffTableModel = new StaffTableModel();
    staffTable = new JTable(staffTableModel);
    staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    buttonsActionsBar = new ButtonsActionsBar(new ButtonsActionsBar.Actions() {
      @Override
      public void onAdd() {
        openAddStaffDialog();
      }

      @Override
      public void onEdit() {
        openEditStaffDialog();
      }

      @Override
      public void onRefresh() {
        refreshStaffTable();
      }
    });

    buttonsActionsBar.setEditEnabled(false);

    staffTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = staffTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(staffTable), BorderLayout.CENTER);

    refreshStaffTable();
  }

  private void refreshStaffTable() {
    try {
      List<Staff> staffMembers = staffController.getAllStaff();
      staffTableModel.setStaffMembers(staffMembers);
      buttonsActionsBar.setEditEnabled(staffTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openAddStaffDialog() {
    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredEditable("staffId", "Staff ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("role", "Role"),
          FormFieldViewConfiguration.optionalEditable("department", "Department"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.optionalEditable("workLocation", "Work Location"),
          FormFieldViewConfiguration.optionalEditable("shiftPattern", "Shift Pattern"),
          FormFieldViewConfiguration.optionalEditable("employmentStatus", "Employment Status"),
          FormFieldViewConfiguration.optionalEditable("startDate", "Start Date (YYYY-MM-DD)"),
          FormFieldViewConfiguration.optionalEditable("supervisorName", "Supervisor Name"),
          FormFieldViewConfiguration.optionalEditable("accessLevel", "Access Level")
      );

      FormDialog formDialog = new FormDialog(owner, "Add Staff", fieldViewConfigurations, Map.of());
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Staff staff = new Staff(
          valuesByKey.getOrDefault("staffId", ""),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("role", ""),
          valuesByKey.getOrDefault("department", ""),
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("workLocation", ""),
          valuesByKey.getOrDefault("shiftPattern", ""),
          valuesByKey.getOrDefault("employmentStatus", ""),
          valuesByKey.getOrDefault("startDate", ""),
          valuesByKey.getOrDefault("supervisorName", ""),
          valuesByKey.getOrDefault("accessLevel", "")
      );

      staffController.addStaff(staff);
      refreshStaffTable();
      selectStaffById(staff.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void openEditStaffDialog() {
    int selectedRowIndex = staffTable.getSelectedRow();
    if (selectedRowIndex < 0) {
      JOptionPane.showMessageDialog(this, "Select a staff member to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Staff selectedStaff = staffTableModel.getStaffAtRow(selectedRowIndex);
    if (selectedStaff == null) {
      JOptionPane.showMessageDialog(this, "Select a staff member to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      Window owner = SwingUtilities.getWindowAncestor(this);

      List<FormFieldViewConfiguration> fieldViewConfigurations = List.of(
          FormFieldViewConfiguration.requiredReadOnly("staffId", "Staff ID"),
          FormFieldViewConfiguration.requiredEditable("fullName", "Full Name"),
          FormFieldViewConfiguration.optionalEditable("email", "Email"),
          FormFieldViewConfiguration.requiredEditable("role", "Role"),
          FormFieldViewConfiguration.optionalEditable("department", "Department"),
          FormFieldViewConfiguration.optionalEditable("phoneNumber", "Phone Number"),
          FormFieldViewConfiguration.optionalEditable("workLocation", "Work Location"),
          FormFieldViewConfiguration.optionalEditable("shiftPattern", "Shift Pattern"),
          FormFieldViewConfiguration.optionalEditable("employmentStatus", "Employment Status"),
          FormFieldViewConfiguration.optionalEditable("startDate", "Start Date (YYYY-MM-DD)"),
          FormFieldViewConfiguration.optionalEditable("supervisorName", "Supervisor Name"),
          FormFieldViewConfiguration.optionalEditable("accessLevel", "Access Level")
      );

      Map<String, String> defaultValuesByKey = new LinkedHashMap<>();
      defaultValuesByKey.put("staffId", selectedStaff.getId());
      defaultValuesByKey.put("fullName", selectedStaff.getFullName());
      defaultValuesByKey.put("email", selectedStaff.getEmail());
      defaultValuesByKey.put("role", selectedStaff.getRole());
      defaultValuesByKey.put("department", selectedStaff.getDepartment());
      defaultValuesByKey.put("phoneNumber", selectedStaff.getPhoneNumber());
      defaultValuesByKey.put("workLocation", selectedStaff.getWorkLocation());
      defaultValuesByKey.put("shiftPattern", selectedStaff.getShiftPattern());
      defaultValuesByKey.put("employmentStatus", selectedStaff.getEmploymentStatus());
      defaultValuesByKey.put("startDate", selectedStaff.getStartDate());
      defaultValuesByKey.put("supervisorName", selectedStaff.getSupervisorName());
      defaultValuesByKey.put("accessLevel", selectedStaff.getAccessLevel());

      FormDialog formDialog = new FormDialog(owner, "Edit Staff", fieldViewConfigurations, defaultValuesByKey);
      formDialog.setVisible(true);

      if (!formDialog.isConfirmed()) {
        return;
      }

      Map<String, String> valuesByKey = formDialog.getValuesByKey();

      Staff updatedStaff = new Staff(
          selectedStaff.getId(),
          valuesByKey.getOrDefault("fullName", ""),
          valuesByKey.getOrDefault("email", ""),
          valuesByKey.getOrDefault("role", ""),
          valuesByKey.getOrDefault("department", ""),
          valuesByKey.getOrDefault("phoneNumber", ""),
          valuesByKey.getOrDefault("workLocation", ""),
          valuesByKey.getOrDefault("shiftPattern", ""),
          valuesByKey.getOrDefault("employmentStatus", ""),
          valuesByKey.getOrDefault("startDate", ""),
          valuesByKey.getOrDefault("supervisorName", ""),
          valuesByKey.getOrDefault("accessLevel", "")
      );

      staffController.updateStaff(updatedStaff);
      refreshStaffTable();
      selectStaffById(updatedStaff.getId());
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void selectStaffById(String staffId) {
    if (staffId == null || staffId.trim().isEmpty()) {
      return;
    }

    for (int rowIndex = 0; rowIndex < staffTableModel.getRowCount(); rowIndex++) {
      Staff staff = staffTableModel.getStaffAtRow(rowIndex);
      if (staff != null && staffId.equals(staff.getId())) {
        staffTable.setRowSelectionInterval(rowIndex, rowIndex);
        staffTable.scrollRectToVisible(staffTable.getCellRect(rowIndex, 0, true));
        return;
      }
    }
  }
}