package ng.optisoft.rosabon.dto;

import lombok.Data;

@Data
public class GeneralResponse {

	private int status;

	private String message;
	
	private Object data;
	
	public GeneralResponse(int status, String message)
	{
		this.status = status;
		this.message = message;
	}

	public GeneralResponse(int status, String message, Object data)
	{
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
