package hms.output;

import hms.model.Clinician;
import hms.model.Patient;
import hms.model.Referral;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReferralProcessingOutputWriter {

  private final Path outputDirectory;
  private final String emailLogFilename;
  private final String ehrLogFilename;

  public ReferralProcessingOutputWriter(Path outputDirectory) {
    this.outputDirectory = outputDirectory;
    this.emailLogFilename = "referral_emails.log";
    this.ehrLogFilename = "ehr_updates.log";
  }

  public void writeReferralDetailsFile(Referral referral) {
    try {
      Files.createDirectories(outputDirectory);
      Path outputFile = outputDirectory.resolve("referral_" + safe(referral.getId()) + ".txt");

      String referralDetails =
          "Referral Details\n"
              + "Referral Identifier: " + safe(referral.getId()) + "\n"
              + "Patient Identifier: " + safe(referral.getPatientId()) + "\n"
              + "Referring Clinician Identifier: " + safe(referral.getReferringClinicianId()) + "\n"
              + "Referred Clinician Identifier: " + safe(referral.getReferredToClinicianId()) + "\n"
              + "Referring Facility Identifier: " + safe(referral.getReferringFacilityId()) + "\n"
              + "Referred Facility Identifier: " + safe(referral.getReferredToFacilityId()) + "\n"
              + "Referral Date: " + safe(referral.getReferralDate()) + "\n"
              + "Urgency Level: " + safe(referral.getUrgencyLevel()) + "\n"
              + "Referral Reason: " + safe(referral.getReferralReason()) + "\n"
              + "Clinical Summary: " + safe(referral.getClinicalSummary()) + "\n"
              + "Requested Investigations: " + safe(referral.getRequestedInvestigations()) + "\n"
              + "Status: " + (referral.getStatus() == null ? "" : referral.getStatus().name()) + "\n"
              + "Appointment Identifier: " + safe(referral.getAppointmentId()) + "\n"
              + "Notes: " + safe(referral.getNotes()) + "\n"
              + "Created Date: " + safe(referral.getCreatedDate()) + "\n"
              + "Last Updated: " + safe(referral.getLastUpdated()) + "\n";

      try (FileWriter writer = new FileWriter(outputFile.toFile(), false)) {
        writer.write(referralDetails);
      }
    } catch (IOException exception) {
      throw new RuntimeException("Failed to write referral details file: " + exception.getMessage(), exception);
    }
  }

  public void appendReferralEmailLog(Referral referral,
                                    Patient patient,
                                    Clinician referredClinician) {
    String referralId = safe(referral.getId());
    Path emailFilePath = outputDirectory.resolve("email_referral_" + referralId + ".txt");

    String logEntry =
        "Referral Email Notification\n"
            + "Referral Identifier: " + referralId + "\n"
            + "Timestamp: " + timestamp() + "\n"
            + "From: noreply@hms.local\n"
            + "To: " + safe(referredClinician.getEmail()) + "\n"
            + "Subject: New Referral for " + safe(patient.getFullName()) + "\n"
            + "Message: Please review referral " + referralId + " for patient " + safe(patient.getFullName()) + "\n";

    writeTextFile(emailFilePath, logEntry);
    appendToLogFile(emailLogFilename, logEntry);
  }

  public void appendElectronicHealthRecordUpdateLog(Referral referral,
                                                   Patient patient) {
    String referralId = safe(referral.getId());
    Path ehrFilePath = outputDirectory.resolve("ehr_update_" + referralId + ".txt");

    String logEntry =
        "Electronic Health Record Update\n"
            + "Referral Identifier: " + referralId + "\n"
            + "Patient Identifier: " + safe(patient.getId()) + "\n"
            + "Timestamp: " + timestamp() + "\n"
            + "Update: Referral created and sent for specialist review\n";

    writeTextFile(ehrFilePath, logEntry);
    appendToLogFile(ehrLogFilename, logEntry);
  }

  private void writeTextFile(Path filePath, String content) {
    try {
      Files.createDirectories(outputDirectory);
      try (FileWriter writer = new FileWriter(filePath.toFile(), false)) {
        writer.write(content);
      }
    } catch (IOException exception) {
      throw new RuntimeException("Failed to write output file: " + exception.getMessage(), exception);
    }
  }

  private void appendToLogFile(String logFilename, String content) {
    try {
      Files.createDirectories(outputDirectory);
      Path logFile = outputDirectory.resolve(logFilename);
      try (FileWriter writer = new FileWriter(logFile.toFile(), true)) {
        writer.write(content);
        writer.write("\n---\n");
      }
    } catch (IOException exception) {
      throw new RuntimeException("Failed to write log file: " + exception.getMessage(), exception);
    }
  }

  private String timestamp() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}