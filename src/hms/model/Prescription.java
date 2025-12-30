package hms.model;

public class Prescription {

	private String id;
	private String patientId;
	private String clinicianId;
	private String medication;
	private String dosage;
	private String pharmacy;
	private String collectionStatus;
	private String dateIssued;

	public Prescription(String id, String patientId, String clinicianId, String medication, String dosage,
			String pharmacy, String collectionStatus, String dateIssued) {
		this.id = id;
		this.patientId = patientId;
		this.clinicianId = clinicianId;
		this.medication = medication;
		this.dosage = dosage;
		this.pharmacy = pharmacy;
		this.collectionStatus = collectionStatus;
		this.dateIssued = dateIssued;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getClinicianId() {
		return clinicianId;
	}

	public void setClinicianId(String clinicianId) {
		this.clinicianId = clinicianId;
	}

	public String getMedication() {
		return medication;
	}

	public void setMedication(String medication) {
		this.medication = medication;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getPharmacy() {
		return pharmacy;
	}

	public void setPharmacy(String pharmacy) {
		this.pharmacy = pharmacy;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}

	@Override
	public String toString() {
		return "Prescription{id='" + id + "', patientId='" + patientId + "', clinicianId='" + clinicianId
				+ "', medication='" + medication + "', dosage='" + dosage + "', pharmacy='" + pharmacy
				+ "', collectionStatus='" + collectionStatus + "', dateIssued='" + dateIssued + "'}";
	}
}