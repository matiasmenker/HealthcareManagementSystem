package hms.output;

import hms.model.Referral;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class ReferralProcessingOutputWriter {

  private final Path outputDirectory;

  public ReferralProcessingOutputWriter(Path outputDirectory) {
    this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory");
  }

  public void writeReferralFile(Referral referral,
                                String patientName,
                                String fromClinicianName,
                                String toClinicianName,
                                String fromFacilityName,
                                String toFacilityName) {
    Objects.requireNonNull(referral, "referral");

    try {
      Files.createDirectories(outputDirectory);

      String fileName = "referral_" + safe(referral.getId()) + ".txt";
      Path filePath = outputDirectory.resolve(fileName);

      String content = buildReferralContent(referral, patientName, fromClinicianName, toClinicianName, fromFacilityName, toFacilityName);

      Files.writeString(
          filePath,
          content,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE
      );
    } catch (IOException exception) {
      throw new IllegalStateException("Failed generating referral output file: " + exception.getMessage());
    }
  }

  public void appendReferralEmailLog(String emailContent) {
    appendLogLine("referral_emails.log", emailContent);
  }

  public void appendEhrUpdatesLog(String ehrUpdateContent) {
    appendLogLine("ehr_updates.log", ehrUpdateContent);
  }

  private void appendLogLine(String fileName, String content) {
    try {
      Files.createDirectories(outputDirectory);

      Path filePath = outputDirectory.resolve(fileName);

      String line = (content == null ? "" : content.trim()) + System.lineSeparator();

      Files.writeString(
          filePath,
          line,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE,
          StandardOpenOption.APPEND
      );
    } catch (IOException exception) {
      throw new IllegalStateException("Failed writing log file: " + exception.getMessage());
    }
  }

  private String buildReferralContent(Referral referral,
                                      String patientName,
                                      String fromClinicianName,
                                      String toClinicianName,
                                      String fromFacilityName,
                                      String toFacilityName) {
    return "REFERRAL\n"
        + "----------------------------------------\n"
        + "Referral ID: " + safe(referral.getId()) + "\n"
        + "Patient ID: " + safe(referral.getPatientId()) + "\n"
        + "Patient Name: " + safe(patientName) + "\n"
        + "Referring Clinician ID: " + safe(referral.getReferringClinicianId()) + "\n"
        + "Referring Clinician Name: " + safe(fromClinicianName) + "\n"
        + "Referred To Clinician ID: " + safe(referral.getReferredToClinicianId()) + "\n"
        + "Referred To Clinician Name: " + safe(toClinicianName) + "\n"
        + "Referring Facility ID: " + safe(referral.getReferringFacilityId()) + "\n"
        + "Referring Facility Name: " + safe(fromFacilityName) + "\n"
        + "Referred To Facility ID: " + safe(referral.getReferredToFacilityId()) + "\n"
        + "Referred To Facility Name: " + safe(toFacilityName) + "\n"
        + "Referral Date: " + safe(referral.getReferralDate()) + "\n"
        + "Urgency Level: " + safe(referral.getUrgencyLevel()) + "\n"
        + "Referral Reason: " + safe(referral.getReferralReason()) + "\n"
        + "Clinical Summary: " + safe(referral.getClinicalSummary()) + "\n"
        + "Requested Investigations: " + safe(referral.getRequestedInvestigations()) + "\n"
        + "Status: " + (referral.getStatus() == null ? "" : referral.getStatus().name()) + "\n"
        + "Appointment ID: " + safe(referral.getAppointmentId()) + "\n"
        + "Notes: " + safe(referral.getNotes()) + "\n"
        + "Created Date: " + safe(referral.getCreatedDate()) + "\n"
        + "Last Updated: " + safe(referral.getLastUpdated()) + "\n";
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}