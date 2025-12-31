package hms.model.singleton;

public class ReferralProcessingResult {

	private final String referralTextFilePath;
	private final String referralEmailsLogPath;
	private final String electronicHealthRecordUpdatesLogPath;

	public ReferralProcessingResult(String referralTextFilePath, String referralEmailsLogPath,
			String electronicHealthRecordUpdatesLogPath) {
		this.referralTextFilePath = referralTextFilePath;
		this.referralEmailsLogPath = referralEmailsLogPath;
		this.electronicHealthRecordUpdatesLogPath = electronicHealthRecordUpdatesLogPath;
	}

	public String getReferralTextFilePath() {
		return referralTextFilePath;
	}

	public String getReferralEmailsLogPath() {
		return referralEmailsLogPath;
	}

	public String getElectronicHealthRecordUpdatesLogPath() {
		return electronicHealthRecordUpdatesLogPath;
	}
}