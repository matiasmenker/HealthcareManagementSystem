package hms.output;

import hms.model.Clinician;
import hms.model.Patient;
import hms.model.Prescription;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class PrescriptionOutputFileGenerator {

	private final Path outputDirectory;

	public PrescriptionOutputFileGenerator(Path outputDirectory) {
		this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory");
	}

	public String generatePrescriptionTextFile(Prescription prescription, Patient patient, Clinician clinician) {
		Objects.requireNonNull(prescription, "prescription");
		Objects.requireNonNull(patient, "patient");
		Objects.requireNonNull(clinician, "clinician");

		ensureOutputDirectoryExists();

		String fileName = "prescription_" + safe(prescription.getId()) + ".txt";
		Path filePath = outputDirectory.resolve(fileName);

		String content = buildPrescriptionText(prescription, patient, clinician);

		try {
			Files.writeString(filePath, content);
			return filePath.toString();
		} catch (IOException exception) {
			throw new RuntimeException("Failed writing prescription output file: " + filePath.toString(), exception);
		}
	}

	private void ensureOutputDirectoryExists() {
		try {
			Files.createDirectories(outputDirectory);
		} catch (IOException exception) {
			throw new RuntimeException("Failed creating output directory: " + outputDirectory.toString(), exception);
		}
	}

	private String buildPrescriptionText(Prescription prescription, Patient patient, Clinician clinician) {
		String lineSeparator = System.lineSeparator();

		return "Prescription Output" + lineSeparator + "Prescription Identifier: " + safe(prescription.getId())
				+ lineSeparator + "Issue Date: " + safe(prescription.getDateIssued()) + lineSeparator + "Status: "
				+ safe(prescription.getCollectionStatus()) + lineSeparator + lineSeparator + "Patient Details"
				+ lineSeparator + "Patient Identifier: " + safe(patient.getId()) + lineSeparator + "Patient Name: "
				+ safe(patient.getFullName()) + lineSeparator + "Patient Email Address: " + safe(patient.getEmail())
				+ lineSeparator + lineSeparator + "Clinician Details" + lineSeparator + "Clinician Identifier: "
				+ safe(clinician.getId()) + lineSeparator + "Clinician Name: " + safe(clinician.getFullName())
				+ lineSeparator + "Clinician Email Address: " + safe(clinician.getEmail()) + lineSeparator
				+ lineSeparator + "Medication Details" + lineSeparator + "Medication: "
				+ safe(prescription.getMedication()) + lineSeparator + "Dosage: " + safe(prescription.getDosage())
				+ lineSeparator + "Pharmacy: " + safe(prescription.getPharmacy()) + lineSeparator;
	}

	private String safe(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}