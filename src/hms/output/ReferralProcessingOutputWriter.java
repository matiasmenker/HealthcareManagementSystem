package hms.output;

import hms.model.Clinician;
import hms.model.Facility;
import hms.model.Patient;
import hms.model.Referral;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class ReferralProcessingOutputWriter {

  private final Path outputDirectory;

  public ReferralProcessingOutputWriter(Path outputDirectory) {
    this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory");
  }

  public String writeReferralTextFile(Referral referral,
                                      Patient patient,
                                      Clinician fromClinician,
                                      Clinician toClinician,
                                      Facility fromFacility,
                                      Facility toFacility) {
    Objects.requireNonNull(referral, "referral");
    Objects.requireNonNull(patient, "patient");
    Objects.requireNonNull(fromClinician, "fromClinician");
    Objects.requireNonNull(toClinician, "toClinician");
    Objects.requireNonNull(fromFacility, "fromFacility");
    Objects.requireNonNull(toFacility, "toFacility");

    ensureOutputDirectoryExists();

    String fileName = "referral_" + safe(referral.getId()) + ".txt";
    Path filePath = outputDirectory.resolve(fileName);

    String content = buildReferralTextFileContent(referral, patient, fromClinician, toClinician, fromFacility, toFacility);

    try {
      Files.writeString(filePath, content);
      return filePath.toString();
    } catch (IOException exception) {
      throw new RuntimeException("Failed writing referral output file: " + filePath.toString(), exception);
    }
  }

  public String appendReferralEmailLog(Referral referral,
                                      Patient patient,
                                      Clinician fromClinician,
                                      Clinician toClinician) {
    Objects.requireNonNull(referral, "referral");
    Objects.requireNonNull(patient, "patient");
    Objects.requireNonNull(fromClinician, "fromClinician");
    Objects.requireNonNull(toClinician, "toClinician");

    ensureOutputDirectoryExists();

    Path logFilePath = outputDirectory.resolve("referral_emails.log");
    String lineSeparator = System.lineSeparator();

    String entry = "Referral Email Simulation" + lineSeparator
        + "Referral Identifier: " + safe(referral.getId()) + lineSeparator
        + "Recipient Email Address: " + safe(toClinician.getEmail()) + lineSeparator
        + "Sender Email Address: " + safe(fromClinician.getEmail()) + lineSeparator
        + "Subject: Referral " + safe(referral.getId()) + " - " + safe(referral.getUrgency()) + lineSeparator
        + "Message: Patient " + safe(patient.getFullName()) + " (Identifier: " + safe(patient.getId()) + "). "
        + safe(referral.getClinicalSummary()) + lineSeparator
        + "----" + lineSeparator;

    try {
      Files.writeString(logFilePath, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      return logFilePath.toString();
    } catch (IOException exception) {
      throw new RuntimeException("Failed appending referral emails log: " + logFilePath.toString(), exception);
    }
  }

  public String appendElectronicHealthRecordUpdatesLog(Referral referral, Patient patient) {
    Objects.requireNonNull(referral, "referral");
    Objects.requireNonNull(patient, "patient");

    ensureOutputDirectoryExists();

    Path logFilePath = outputDirectory.resolve("electronic_health_record_updates.log");
    String lineSeparator = System.lineSeparator();

    String entry = "Electronic Health Record Update Simulation" + lineSeparator
        + "Patient Identifier: " + safe(patient.getId()) + lineSeparator
        + "Patient Name: " + safe(patient.getFullName()) + lineSeparator
        + "Referral Identifier: " + safe(referral.getId()) + lineSeparator
        + "Referral Status: " + safe(referral.getStatus()) + lineSeparator
        + "Referral Creation Date: " + safe(referral.getDateCreated()) + lineSeparator
        + "----" + lineSeparator;

    try {
      Files.writeString(logFilePath, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      return logFilePath.toString();
    } catch (IOException exception) {
      throw new RuntimeException("Failed appending electronic health record updates log: " + logFilePath.toString(), exception);
    }
  }

  private void ensureOutputDirectoryExists() {
    try {
      Files.createDirectories(outputDirectory);
    } catch (IOException exception) {
      throw new RuntimeException("Failed creating output directory: " + outputDirectory.toString(), exception);
    }
  }

  private String buildReferralTextFileContent(Referral referral,
                                             Patient patient,
                                             Clinician fromClinician,
                                             Clinician toClinician,
                                             Facility fromFacility,
                                             Facility toFacility) {
    String lineSeparator = System.lineSeparator();

    return "Referral Output" + lineSeparator
        + "Referral Identifier: " + safe(referral.getId()) + lineSeparator
        + "Referral Status: " + safe(referral.getStatus()) + lineSeparator
        + "Urgency: " + safe(referral.getUrgency()) + lineSeparator
        + "Creation Date: " + safe(referral.getDateCreated()) + lineSeparator
        + lineSeparator
        + "Patient Details" + lineSeparator
        + "Patient Identifier: " + safe(patient.getId()) + lineSeparator
        + "Patient Name: " + safe(patient.getFullName()) + lineSeparator
        + "Patient Email Address: " + safe(patient.getEmail()) + lineSeparator
        + "Patient National Health Service Number: " + safe(patient.getNhsNumber()) + lineSeparator
        + lineSeparator
        + "Referring Clinician" + lineSeparator
        + "Clinician Identifier: " + safe(fromClinician.getId()) + lineSeparator
        + "Clinician Name: " + safe(fromClinician.getFullName()) + lineSeparator
        + "Clinician Email Address: " + safe(fromClinician.getEmail()) + lineSeparator
        + lineSeparator
        + "Receiving Clinician" + lineSeparator
        + "Clinician Identifier: " + safe(toClinician.getId()) + lineSeparator
        + "Clinician Name: " + safe(toClinician.getFullName()) + lineSeparator
        + "Clinician Email Address: " + safe(toClinician.getEmail()) + lineSeparator
        + lineSeparator
        + "Referring Facility" + lineSeparator
        + "Facility Identifier: " + safe(fromFacility.getId()) + lineSeparator
        + "Facility Name: " + safe(fromFacility.getName()) + lineSeparator
        + "Facility Type: " + safe(fromFacility.getType()) + lineSeparator
        + lineSeparator
        + "Receiving Facility" + lineSeparator
        + "Facility Identifier: " + safe(toFacility.getId()) + lineSeparator
        + "Facility Name: " + safe(toFacility.getName()) + lineSeparator
        + "Facility Type: " + safe(toFacility.getType()) + lineSeparator
        + lineSeparator
        + "Clinical Summary" + lineSeparator
        + safe(referral.getClinicalSummary()) + lineSeparator;
  }

  private String safe(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}