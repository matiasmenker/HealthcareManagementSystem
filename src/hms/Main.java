package hms;

import hms.controller.AppointmentController;
import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.controller.StaffController;
import hms.repository.AppointmentRepository;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;
import hms.repository.StaffRepository;
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

        ClinicianRepository clinicianRepository = new ClinicianRepository();
        clinicianRepository.load(dataDirectory.resolve("clinicians.csv").toString());

        AppointmentRepository appointmentRepository = new AppointmentRepository();
        appointmentRepository.load(dataDirectory.resolve("appointments.csv").toString());

        FacilityRepository facilityRepository = new FacilityRepository();
        facilityRepository.load(dataDirectory.resolve("facilities.csv").toString());

        StaffRepository staffRepository = new StaffRepository();
        staffRepository.load(dataDirectory.resolve("staff.csv").toString());

        PatientController patientController = new PatientController(patientRepository);
        ClinicianController clinicianController = new ClinicianController(clinicianRepository);
        AppointmentController appointmentController = new AppointmentController(appointmentRepository, patientRepository);
        FacilityController facilityController = new FacilityController(facilityRepository);
        StaffController staffController = new StaffController(staffRepository);

        mainFrame = new MainFrame(
            patientController,
            clinicianController,
            appointmentController,
            facilityController,
            staffController
        );
        mainFrame.setVisible(true);

        Map<String, Integer> recordCountsByLabel = loadAllCsvRecordCounts(
            dataDirectory,
            patientRepository.findAll().size(),
            clinicianRepository.findAll().size(),
            appointmentRepository.findAll().size(),
            facilityRepository.findAll().size(),
            staffRepository.findAll().size()
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

  private static Map<String, Integer> loadAllCsvRecordCounts(Path dataDirectory,
                                                             int patientsCount,
                                                             int cliniciansCount,
                                                             int appointmentsCount,
                                                             int facilitiesCount,
                                                             int staffCount) {
    Map<String, String> labelsByFileName = new LinkedHashMap<>();
    labelsByFileName.put("prescriptions", "prescriptions.csv");
    labelsByFileName.put("referrals", "referrals.csv");

    Map<String, Integer> countsByLabel = new LinkedHashMap<>();
    countsByLabel.put("patients", patientsCount);
    countsByLabel.put("clinicians", cliniciansCount);
    countsByLabel.put("appointments", appointmentsCount);
    countsByLabel.put("facilities", facilitiesCount);
    countsByLabel.put("staff", staffCount);

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