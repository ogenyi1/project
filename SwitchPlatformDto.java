package ng.optisoft.rosabon.dto;

import lombok.Data;
import ng.optisoft.rosabon.model.Useraccount;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SwitchPlatformDto {
    @NotNull(message = "user type is required")
    @NotEmpty(message = "user type is required")
    private String userType;

    public static Useraccount updateEntity(SwitchPlatformDto dto, Useraccount useraccount) {
        if(dto == null){
            return null;
        }
        useraccount.setUserType(dto.getUserType());

        return  useraccount;
    }
}
