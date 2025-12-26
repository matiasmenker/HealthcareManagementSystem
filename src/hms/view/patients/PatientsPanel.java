package hms.view.patients;

import hms.controller.PatientController;
import hms.model.Patient;
import hms.view.common.ButtonsActionsBar;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.util.List;
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
        JOptionPane.showMessageDialog(PatientsPanel.this, "Add not implemented yet");
      }

      @Override
      public void onEdit() {
        JOptionPane.showMessageDialog(PatientsPanel.this, "Edit not implemented yet");
      }

      @Override
      public void onDelete() {
        JOptionPane.showMessageDialog(PatientsPanel.this, "Delete not implemented yet");
      }

      @Override
      public void onRefresh() {
        refreshPatientsTable();
      }
    });

    buttonsActionsBar.setEditAndDeleteEnabled(false);

    patientsTable.getSelectionModel().addListSelectionListener(event -> {
      if (event.getValueIsAdjusting()) {
        return;
      }
      boolean hasSelection = patientsTable.getSelectedRow() >= 0;
      buttonsActionsBar.setEditAndDeleteEnabled(hasSelection);
    });

    add(buttonsActionsBar, BorderLayout.NORTH);
    add(new JScrollPane(patientsTable), BorderLayout.CENTER);

    refreshPatientsTable();
  }

  private void refreshPatientsTable() {
    try {
      List<Patient> patients = patientController.getAllPatients();
      patientsTableModel.setPatients(patients);
      buttonsActionsBar.setEditAndDeleteEnabled(patientsTable.getSelectedRow() >= 0);
    } catch (RuntimeException exception) {
      JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}