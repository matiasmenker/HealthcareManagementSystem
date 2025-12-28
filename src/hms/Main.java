package hms;

import hms.controller.AppointmentController;
import hms.controller.PatientController;
import hms.repository.AppointmentRepository;
import hms.repository.PatientRepository;
import hms.util.CsvFileReader;
import hms.view.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = null;

      try {
        Path dataDirectory = Path.of(System.getProperty("user.dir")).resolve("data");

        PatientRepository patientRepository = new PatientRepository();
        patientRepository.load(dataDirectory.resolve("patients.csv").toString());

        AppointmentRepository appointmentRepository = new AppointmentRepository();
        appointmentRepository.load(dataDirectory.resolve("appointments.csv").toString());

        PatientController patientController = new PatientController(patientRepository);
        AppointmentController appointmentController = new AppointmentController(appointmentRepository, patientRepository);

        mainFrame = new MainFrame(patientController, appointmentController);
        mainFrame.setVisible(true);

        Map<String, Integer> recordCountsByLabel = loadAllCsvRecordCounts(
            dataDirectory,
            patientRepository.findAll().size(),
            appointmentRepository.findAll().size()
        );

        mainFrame.setStatusText(buildStatusText(recordCountsByLabel));
      } catch (RuntimeException exception) {
        if (mainFrame != null) {
          mainFrame.setStatusText("Failed loading CSV files");
        }

        JOptionPane.showMessageDialog(
            mainFrame,
            exception.getMessage(),
            "Startup error",
            JOptionPane.ERROR_MESSAGE
        );
      }
    });
  }

  private static Map<String, Integer> loadAllCsvRecordCounts(Path dataDirectory, int patientsCount, int appointmentsCount) {
    Map<String, String> labelsByFileName = new LinkedHashMap<>();
    labelsByFileName.put("clinicians", "clinicians.csv");
    labelsByFileName.put("facilities", "facilities.csv");
    labelsByFileName.put("prescriptions", "prescriptions.csv");
    labelsByFileName.put("referrals", "referrals.csv");
    labelsByFileName.put("staff", "staff.csv");

    Map<String, Integer> countsByLabel = new LinkedHashMap<>();
    countsByLabel.put("patients", patientsCount);
    countsByLabel.put("appointments", appointmentsCount);

    for (Map.Entry<String, String> entry : labelsByFileName.entrySet()) {
      String label = entry.getKey();
      String fileName = entry.getValue();
      String filePath = dataDirectory.resolve(fileName).toString();

      List<Map<String, String>> rows = CsvFileReader.readRowsAsMaps(filePath);
      countsByLabel.put(label, rows.size());
    }

    return countsByLabel;
  }

  private static String buildStatusText(Map<String, Integer> countsByLabel) {
    return "Loaded "
        + countsByLabel.getOrDefault("patients", 0) + " patients, "
        + countsByLabel.getOrDefault("clinicians", 0) + " clinicians, "
        + countsByLabel.getOrDefault("facilities", 0) + " facilities, "
        + countsByLabel.getOrDefault("appointments", 0) + " appointments, "
        + countsByLabel.getOrDefault("prescriptions", 0) + " prescriptions, "
        + countsByLabel.getOrDefault("referrals", 0) + " referrals, "
        + countsByLabel.getOrDefault("staff", 0) + " staff";
  }
}