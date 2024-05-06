package ng.optisoft.rosabon.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ResetPasswordDto {
   @NotEmpty(message = "Email is required")
   private String email;
   @NotEmpty(message = "Confirm password is required")
   private String confirmPassword;
   @NotEmpty(message = "New password is required")
   private String newPassword;
}
