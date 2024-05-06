package ng.optisoft.rosabon.dto.request;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordInDto {

    @NotEmpty(message = "New password is required")
    private String newPassword;
    
    @NotEmpty(message = "Old password is required")
    private String oldPassword;
    
    @NotEmpty(message = MessageConstant.OTP_REQUIRED)
    private String otp;
}
