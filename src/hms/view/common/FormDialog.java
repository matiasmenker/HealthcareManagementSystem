package hms.view.common;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FormDialog extends JDialog {
  private final Map<String, JTextField> textFieldsByKey = new LinkedHashMap<>();
  private boolean confirmed;

  public FormDialog(Window owner,
                    String title,
                    List<FormFieldViewConfiguration> fieldViewConfigurations,
                    Map<String, String> defaultValuesByKey) {
    super(owner, title, ModalityType.APPLICATION_MODAL);
    Objects.requireNonNull(fieldViewConfigurations, "fieldViewConfigurations");

    confirmed = false;

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(4, 4, 4, 4);
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.HORIZONTAL;

    int rowIndex = 0;
    for (FormFieldViewConfiguration fieldViewConfiguration : fieldViewConfigurations) {
      constraints.gridy = rowIndex;

      constraints.gridx = 0;
      constraints.weightx = 0.0;
      JLabel label = new JLabel(fieldViewConfiguration.getLabel() + (fieldViewConfiguration.isRequired() ? " *" : ""));
      formPanel.add(label, constraints);

      constraints.gridx = 1;
      constraints.weightx = 1.0;
      JTextField textField = new JTextField(28);

      String defaultValue = "";
      if (defaultValuesByKey != null) {
        defaultValue = defaultValuesByKey.getOrDefault(fieldViewConfiguration.getKey(), "");
      }

      textField.setText(defaultValue == null ? "" : defaultValue);
      textField.setEditable(fieldViewConfiguration.isEditable());

      formPanel.add(textField, constraints);
      textFieldsByKey.put(fieldViewConfiguration.getKey(), textField);

      rowIndex++;
    }

    add(new JScrollPane(formPanel), BorderLayout.CENTER);

    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton cancelButton = new JButton("Cancel");
    JButton okButton = new JButton("OK");

    cancelButton.addActionListener(e -> dispose());
    okButton.addActionListener(e -> {
      String validationError = validateRequiredFields(fieldViewConfigurations);
      if (validationError != null) {
        JOptionPane.showMessageDialog(this, validationError, "Validation", JOptionPane.WARNING_MESSAGE);
        return;
      }
      confirmed = true;
      dispose();
    });

    buttonsPanel.add(cancelButton);
    buttonsPanel.add(okButton);

    add(buttonsPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(owner);
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public Map<String, String> getValuesByKey() {
    Map<String, String> valuesByKey = new LinkedHashMap<>();
    for (Map.Entry<String, JTextField> entry : textFieldsByKey.entrySet()) {
      String value = entry.getValue().getText();
      valuesByKey.put(entry.getKey(), value == null ? "" : value.trim());
    }
    return valuesByKey;
  }

  private String validateRequiredFields(List<FormFieldViewConfiguration> fieldViewConfigurations) {
    for (FormFieldViewConfiguration fieldViewConfiguration : fieldViewConfigurations) {
      if (!fieldViewConfiguration.isRequired()) {
        continue;
      }
      JTextField textField = textFieldsByKey.get(fieldViewConfiguration.getKey());
      if (textField == null) {
        continue;
      }
      String value = textField.getText();
      if (value == null || value.trim().isEmpty()) {
        return "Field '" + fieldViewConfiguration.getLabel() + "' is required.";
      }
    }
    return null;
  }
}