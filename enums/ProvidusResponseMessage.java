package ng.optisoft.rosabon.enums;

import lombok.Getter;

@Getter
public enum ProvidusResponseMessage {
	SUCCESS("success"),
	DUPLICATE_TRANSACTION("Duplicate Transaction"),
	REJECTED_TRANSACTION("Rejected Transaction"),
	SYSTEM_FAILURE_RETRY("Rejected Transaction");

	private String message;
	ProvidusResponseMessage(String s) {
		message = s;
	}

}
