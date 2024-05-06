package ng.optisoft.rosabon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCountDto {
	private String message;
	private int loginCount;
	private int failedCount;
	@Override
	public String toString() {
		return "[message=" + message + ", loginCount=" + loginCount + ", failedCount=" + failedCount + "]";
	}
}
