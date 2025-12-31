package hms.view.common;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FormDialog extends JDialog {

	private final List<FormFieldViewConfiguration> fieldViewConfigurations;
	private final Map<String, JTextField> textFieldsByKey = new LinkedHashMap<>();
	private final Map<String, JComboBox<SelectionItem>> selectionBoxesByKey = new LinkedHashMap<>();
	private boolean confirmed;

	public FormDialog(Window owner, String title, List<FormFieldViewConfiguration> fieldViewConfigurations,
			Map<String, String> defaultValuesByKey) {
		super(owner, title, ModalityType.APPLICATION_MODAL);

		this.fieldViewConfigurations = fieldViewConfigurations == null ? new ArrayList<>()
				: new ArrayList<>(fieldViewConfigurations);
		this.confirmed = false;

		setLayout(new BorderLayout(10, 10));
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(6, 6, 6, 6);

		for (FormFieldViewConfiguration configuration : this.fieldViewConfigurations) {
			JLabel label = new JLabel(configuration.getLabel());
			constraints.gridx = 0;
			constraints.weightx = 0;
			constraints.fill = GridBagConstraints.NONE;
			formPanel.add(label, constraints);

			constraints.gridx = 1;
			constraints.weightx = 1;
			constraints.fill = GridBagConstraints.HORIZONTAL;

			String defaultValue = defaultValuesByKey == null ? ""
					: defaultValuesByKey.getOrDefault(configuration.getKey(), "");
			if (configuration.getInputType() == FormFieldInputType.TEXT) {
				JTextField textField = new JTextField();
				textField.setPreferredSize(new Dimension(320, 26));
				textField.setEditable(configuration.isEditable());
				textField.setText(defaultValue == null ? "" : defaultValue);
				formPanel.add(textField, constraints);
				textFieldsByKey.put(configuration.getKey(), textField);
			} else {
				List<SelectionItem> items = buildSelectionItems(configuration);
				JComboBox<SelectionItem> selectionBox = new JComboBox<>(items.toArray(new SelectionItem[0]));
				selectionBox.setEnabled(configuration.isEditable());
				selectionBox.setPreferredSize(new Dimension(320, 26));
				selectDefaultSelectionItem(selectionBox, defaultValue);
				formPanel.add(selectionBox, constraints);
				selectionBoxesByKey.put(configuration.getKey(), selectionBox);
			}

			constraints.gridy++;
		}

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("OK");

		cancelButton.addActionListener(event -> {
			confirmed = false;
			setVisible(false);
			dispose();
		});

		okButton.addActionListener(event -> {
			try {
				validateRequiredFields();
				confirmed = true;
				setVisible(false);
				dispose();
			} catch (RuntimeException exception) {
				JOptionPane.showMessageDialog(this, exception.getMessage(), "Validation error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);

		add(formPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(owner);
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public Map<String, String> getValuesByKey() {
		Map<String, String> valuesByKey = new LinkedHashMap<>();

		for (FormFieldViewConfiguration configuration : fieldViewConfigurations) {
			String key = configuration.getKey();

			if (configuration.getInputType() == FormFieldInputType.TEXT) {
				JTextField textField = textFieldsByKey.get(key);
				String value = textField == null ? "" : textField.getText();
				valuesByKey.put(key, value == null ? "" : value.trim());
			} else {
				JComboBox<SelectionItem> selectionBox = selectionBoxesByKey.get(key);
				SelectionItem selectedItem = selectionBox == null ? null
						: (SelectionItem) selectionBox.getSelectedItem();
				String value = selectedItem == null ? "" : selectedItem.getValue();
				valuesByKey.put(key, value == null ? "" : value.trim());
			}
		}

		return valuesByKey;
	}

	private List<SelectionItem> buildSelectionItems(FormFieldViewConfiguration configuration) {
		Objects.requireNonNull(configuration, "configuration");

		List<SelectionItem> items = configuration.getSelectionItems();

		if (!configuration.isRequired()) {
			boolean hasEmpty = false;
			for (SelectionItem item : items) {
				if (item != null && (item.getValue() == null || item.getValue().trim().isEmpty())) {
					hasEmpty = true;
					break;
				}
			}
			if (!hasEmpty) {
				List<SelectionItem> withEmpty = new ArrayList<>();
				withEmpty.add(new SelectionItem("", ""));
				withEmpty.addAll(items);
				return withEmpty;
			}
		}

		return items;
	}

	private void selectDefaultSelectionItem(JComboBox<SelectionItem> selectionBox, String defaultValue) {
		if (selectionBox == null) {
			return;
		}
		String desired = defaultValue == null ? "" : defaultValue.trim();
		for (int index = 0; index < selectionBox.getItemCount(); index++) {
			SelectionItem item = selectionBox.getItemAt(index);
			if (item == null) {
				continue;
			}
			if (Objects.equals(item.getValue(), desired)) {
				selectionBox.setSelectedIndex(index);
				return;
			}
		}
	}

	private void validateRequiredFields() {
		for (FormFieldViewConfiguration configuration : fieldViewConfigurations) {
			if (!configuration.isRequired()) {
				continue;
			}

			String key = configuration.getKey();

			if (configuration.getInputType() == FormFieldInputType.TEXT) {
				JTextField textField = textFieldsByKey.get(key);
				String value = textField == null ? "" : textField.getText();
				if (value == null || value.trim().isEmpty()) {
					throw new IllegalArgumentException(configuration.getLabel() + " is required");
				}
			} else {
				JComboBox<SelectionItem> selectionBox = selectionBoxesByKey.get(key);
				SelectionItem selectedItem = selectionBox == null ? null
						: (SelectionItem) selectionBox.getSelectedItem();
				String value = selectedItem == null ? "" : selectedItem.getValue();
				if (value == null || value.trim().isEmpty()) {
					throw new IllegalArgumentException(configuration.getLabel() + " is required");
				}
			}
		}
	}
}