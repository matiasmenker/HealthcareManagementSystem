package hms.view.common;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.Objects;

public class ButtonsActionsBar extends JPanel {

	public interface Actions {
		void onAdd();

		void onEdit();

		void onRefresh();
	}

	private final JButton addButton;
	private final JButton editButton;
	private final JButton deleteButton;
	private final JButton refreshButton;

	private Runnable deleteHandler;

	public ButtonsActionsBar(Actions actions) {
		Objects.requireNonNull(actions, "actions");

		setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));

		addButton = new JButton("Add");
		editButton = new JButton("Edit");
		deleteButton = new JButton("Delete");
		refreshButton = new JButton("Refresh");

		add(addButton);
		add(editButton);
		add(deleteButton);
		add(refreshButton);

		addButton.addActionListener(event -> actions.onAdd());
		editButton.addActionListener(event -> actions.onEdit());
		refreshButton.addActionListener(event -> actions.onRefresh());

		deleteButton.addActionListener(event -> {
			if (deleteHandler != null) {
				deleteHandler.run();
			}
		});

		deleteButton.setVisible(false);
		deleteButton.setEnabled(false);
	}

	public void setEditEnabled(boolean enabled) {
		editButton.setEnabled(enabled);
	}

	public void setDeleteEnabled(boolean enabled) {
		if (!deleteButton.isVisible()) {
			deleteButton.setEnabled(false);
			return;
		}
		deleteButton.setEnabled(enabled);
	}

	public void setDeleteHandler(Runnable deleteHandler) {
		this.deleteHandler = deleteHandler;
		deleteButton.setVisible(true);
		deleteButton.setEnabled(true);
		revalidate();
		repaint();
	}
}