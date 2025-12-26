package hms.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {

	private final JLabel statusBarLabel;

	public MainFrame() {
		setTitle("Healthcare Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 650);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		statusBarLabel = new JLabel("Ready", SwingConstants.LEFT);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Patients", new JLabel("Patients screen not implemented yet"));
		tabbedPane.addTab("Clinicians", new JLabel("Clinicians screen not implemented yet"));
		tabbedPane.addTab("Appointments", new JLabel("Appointments screen not implemented yet"));
		tabbedPane.addTab("Prescriptions", new JLabel("Prescriptions screen not implemented yet"));
		tabbedPane.addTab("Referrals", new JLabel("Referrals screen not implemented yet"));
		tabbedPane.addTab("Facilities", new JLabel("Facilities screen not implemented yet"));
		tabbedPane.addTab("Staff", new JLabel("Staff screen not implemented yet"));

		add(tabbedPane, BorderLayout.CENTER);
		add(statusBarLabel, BorderLayout.SOUTH);
	}

	public void setStatusText(String text) {
		statusBarLabel.setText(text);
	}
}