package hms.output;

import hms.model.Prescription;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class PrescriptionOutputFileGenerator {

	private final Path outputDirectory;

	public PrescriptionOutputFileGenerator(Path outputDirectory) {
		this.outputDirectory = Objects.requireNonNull(outputDirectory, "outputDirectory");
	}

	public void generatePrescriptionFile(Prescription prescription) {
		Objects.requireNonNull(prescription, "prescription");

		try {
			Files.createDirectories(outputDirectory);

			String fileName = "prescription_" + prescription.getId() + ".txt";
			Path filePath = outputDirectory.resolve(fileName);

			String content = buildPrescriptionContent(prescription);

			Files.writeString(filePath, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE);
		} catch (IOException exception) {
			throw new IllegalStateException("Failed generating prescription output file: " + exception.getMessage());
		}
	}

	private String buildPrescriptionContent(Prescription prescription) {
		return "PRESCRIPTION\n" + "----------------------------------------\n" + "Prescription ID: "
				+ safe(prescription.getId()) + "\n" + "Patient ID: " + safe(prescription.getPatientId()) + "\n"
				+ "Clinician ID: " + safe(prescription.getClinicianId()) + "\n" + "Medication: "
				+ safe(prescription.getMedication()) + "\n" + "Dosage: " + safe(prescription.getDosage()) + "\n"
				+ "Pharmacy: " + safe(prescription.getPharmacy()) + "\n" + "Collection Status: "
				+ safe(prescription.getCollectionStatus()) + "\n" + "Date Issued: " + safe(prescription.getDateIssued())
				+ "\n";
	}

	private String safe(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}