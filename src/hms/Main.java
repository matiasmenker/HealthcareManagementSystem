package hms;

import hms.controller.AppointmentController;
import hms.controller.ClinicianController;
import hms.controller.FacilityController;
import hms.controller.PatientController;
import hms.controller.PrescriptionController;
import hms.controller.ReferralController;
import hms.controller.StaffController;
import hms.model.singleton.ReferralManager;
import hms.output.PrescriptionOutputFileGenerator;
import hms.output.ReferralProcessingOutputWriter;
import hms.repository.AppointmentRepository;
import hms.repository.ClinicianRepository;
import hms.repository.FacilityRepository;
import hms.repository.PatientRepository;
import hms.repository.PrescriptionRepository;
import hms.repository.ReferralRepository;
import hms.repository.StaffRepository;
import hms.view.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = null;

      try {
        Path projectDirectory = Path.of(System.getProperty("user.dir"));
        Path dataDirectory = projectDirectory.resolve("data");
        Path outputDirectory = projectDirectory.resolve("output");

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

        PrescriptionRepository prescriptionRepository = new PrescriptionRepository();
        prescriptionRepository.load(dataDirectory.resolve("prescriptions.csv").toString());

        ReferralRepository referralRepository = new ReferralRepository();
        referralRepository.load(dataDirectory.resolve("referrals.csv").toString());

        PatientController patientController = new PatientController(patientRepository);
        ClinicianController clinicianController = new ClinicianController(clinicianRepository);
        FacilityController facilityController = new FacilityController(facilityRepository);

        AppointmentController appointmentController = new AppointmentController(
            appointmentRepository,
            patientRepository,
            clinicianRepository,
            facilityRepository
        );

        StaffController staffController = new StaffController(staffRepository);

        PrescriptionOutputFileGenerator prescriptionOutputFileGenerator = new PrescriptionOutputFileGenerator(outputDirectory);
        PrescriptionController prescriptionController = new PrescriptionController(
            prescriptionRepository,
            patientRepository,
            clinicianRepository,
            prescriptionOutputFileGenerator
        );

        ReferralProcessingOutputWriter referralProcessingOutputWriter = new ReferralProcessingOutputWriter(outputDirectory);
        ReferralManager.getInstance(
            referralRepository,
            patientRepository,
            clinicianRepository,
            facilityRepository,
            referralProcessingOutputWriter
        );
        
        ReferralManager referralManager = ReferralManager.getInstance(
        	    referralRepository,
        	    patientRepository,
        	    clinicianRepository,
        	    facilityRepository,
        	    referralProcessingOutputWriter
        	);

        ReferralController referralController = new ReferralController(
        	    referralManager,
        	    referralRepository,
        	    patientRepository,
        	    clinicianRepository,
        	    facilityRepository
        	);

        mainFrame = new MainFrame(
            patientController,
            clinicianController,
            appointmentController,
            facilityController,
            staffController,
            prescriptionController,
            referralController
        );
        mainFrame.setVisible(true);

        logStartupInformation(
            mainFrame,
            dataDirectory,
            outputDirectory,
            patientRepository.findAll().size(),
            clinicianRepository.findAll().size(),
            facilityRepository.findAll().size(),
            appointmentRepository.findAll().size(),
            prescriptionRepository.findAll().size(),
            referralRepository.findAll().size(),
            staffRepository.findAll().size()
        );
      } catch (RuntimeException exception) {
        if (mainFrame != null) {
          mainFrame.setStatusText("Startup error: " + exception.getMessage());
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

  private static void logStartupInformation(MainFrame mainFrame,
                                           Path dataDirectory,
                                           Path outputDirectory,
                                           int patientsCount,
                                           int cliniciansCount,
                                           int facilitiesCount,
                                           int appointmentsCount,
                                           int prescriptionsCount,
                                           int referralsCount,
                                           int staffCount) {
    mainFrame.setStatusText("Application started");
    mainFrame.setStatusText("Data directory: " + dataDirectory.toString());
    mainFrame.setStatusText("Output directory: " + outputDirectory.toString());
    mainFrame.setStatusText("Loaded patients: " + patientsCount);
    mainFrame.setStatusText("Loaded clinicians: " + cliniciansCount);
    mainFrame.setStatusText("Loaded facilities: " + facilitiesCount);
    mainFrame.setStatusText("Loaded appointments: " + appointmentsCount);
    mainFrame.setStatusText("Loaded prescriptions: " + prescriptionsCount);
    mainFrame.setStatusText("Loaded referrals: " + referralsCount);
    mainFrame.setStatusText("Loaded staff: " + staffCount);
  }
}