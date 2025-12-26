package hms.view.common;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class ButtonsActionsBar extends JPanel {

  public interface Actions {
    void onAdd();
    void onEdit();
    void onDelete();
    void onRefresh();
  }

  private final JButton addButton;
  private final JButton editButton;
  private final JButton deleteButton;
  private final JButton refreshButton;

  public ButtonsActionsBar(Actions actions) {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    addButton = new JButton("Add");
    editButton = new JButton("Edit");
    deleteButton = new JButton("Delete");
    refreshButton = new JButton("Refresh");

    addButton.addActionListener(e -> actions.onAdd());
    editButton.addActionListener(e -> actions.onEdit());
    deleteButton.addActionListener(e -> actions.onDelete());
    refreshButton.addActionListener(e -> actions.onRefresh());

    add(addButton);
    add(editButton);
    add(deleteButton);
    add(refreshButton);
  }

  public void setEditAndDeleteEnabled(boolean enabled) {
    editButton.setEnabled(enabled);
    deleteButton.setEnabled(enabled);
  }
}