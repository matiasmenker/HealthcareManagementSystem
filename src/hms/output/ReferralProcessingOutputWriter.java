package hms.output;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReferralProcessingOutputWriter {

  private final Path outputDirectory;
  private final Path referralEmailsLogPath;
  private final Path electronicHealthRecordUpdatesLogPath;

  public ReferralProcessingOutputWriter(Path outputDirectory) {
    if (outputDirectory == null) {
      throw new IllegalArgumentException("outputDirectory is required");
    }
    this.outputDirectory = outputDirectory;
    this.referralEmailsLogPath = outputDirectory.resolve("referral_emails.log");
    this.electronicHealthRecordUpdatesLogPath = outputDirectory.resolve("ehr_updates.log");
    ensureOutputDirectoryExists();
  }

  public void writeReferralTextFile(Referral referral,
                                   Patient patient,
                                   Clinician referringClinician,
                                   Clinician referredClinician,
                                   Facility referringFacility,
                                   Facility referredFacility) {
    String referralId = safe(referral.getId());
    Path referralFilePath = outputDirectory.resolve("referral_" + referralId + ".txt");

    String content =
        "Referral Document\n"
            + "Referral Identifier: " + referralId + "\n"
            + "Patient: " + safe(patient == null ? "" : patient.getFullName()) + " (ID: " + safe(referral.getPatientId()) + ")\n"
            + "Referring Clinician: " + safe(referringClinician == null ? "" : referringClinician.getFullName()) + " (ID: " + safe(referral.getReferringClinicianId()) + ")\n"
            + "Referred Clinician: " + safe(referredClinician == null ? "" : referredClinician.getFullName()) + " (ID: " + safe(referral.getReferredToClinicianId()) + ")\n"
            + "Referring Facility: " + safe(referringFacility == null ? "" : referringFacility.getName()) + " (ID: " + safe(referral.getReferringFacilityId()) + ")\n"
            + "Referred Facility: " + safe(referredFacility == null ? "" : referredFacility.getName()) + " (ID: " + safe(referral.getReferredToFacilityId()) + ")\n"
            + "Referral Date: " + safe(referral.getReferralDate()) + "\n"
            + "Urgency Level: " + safe(referral.getUrgencyLevel()) + "\n"
            + "Referral Reason: " + safe(referral.getReferralReason()) + "\n"
            + "Clinical Summary: " + safe(referral.getClinicalSummary()) + "\n"
            + "Requested Investigations: " + safe(referral.getRequestedInvestigations()) + "\n"
            + "Notes: " + safe(referral.getNotes()) + "\n"
            + "Status: " + (referral.getStatus() == null ? "" : referral.getStatus().name()) + "\n"
            + "Created Date: " + safe(referral.getCreatedDate()) + "\n"
            + "Last Updated: " + safe(referral.getLastUpdated()) + "\n";

    writeFile(referralFilePath, content);
  }

  public void appendReferralEmailLog(Referral referral,
                                    Patient patient,
                                    Clinician referredClinician) {
    String logEntry =
        "Referral Email Notification\n"
            + "Referral Identifier: " + safe(referral.getId()) + "\n"
            + "Patient: " + safe(patient == null ? "" : patient.getFullName()) + "\n"
            + "Recipient Clinician: " + safe(referredClinician == null ? "" : referredClinician.getFullName()) + "\n"
            + "Message: A new referral has been created and requires your attention.\n"
            + "\n";

    appendFile(referralEmailsLogPath, logEntry);
  }

  public void appendElectronicHealthRecordUpdateLog(Referral referral,
                                                   Patient patient) {
    String logEntry =
        "Electronic Health Record Update\n"
            + "Referral Identifier: " + safe(referral.getId()) + "\n"
            + "Patient: " + safe(patient == null ? "" : patient.getFullName()) + "\n"
            + "Update: Referral recorded in patient health record.\n"
            + "\n";

    appendFile(electronicHealthRecordUpdatesLogPath, logEntry);
  }

  private void ensureOutputDirectoryExists() {
    try {
      Files.createDirectories(outputDirectory);
    } catch (IOException exception) {
      throw new RuntimeException("Failed creating output directory: " + outputDirectory);
    }
  }

  private void writeFile(Path path, String content) {
    try {
      Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException("Failed writing file: " + path + " (" + exception.getMessage() + ")");
    }
  }

  private void appendFile(Path path, String content) {
    try {
      Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException exception) {
      throw new RuntimeException("Failed appending file: " + path + " (" + exception.getMessage() + ")");
    }
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}