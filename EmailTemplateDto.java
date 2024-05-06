package ng.optisoft.rosabon.dto;

import lombok.Data;
import ng.optisoft.rosabon.constant.MessageConstant;
import ng.optisoft.rosabon.util.ToStringUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmailTemplateDto {

    @NotEmpty(message = "Mail-to required")
    @Email(message = MessageConstant.INVALID_EMAIL)
    private String mailTo;

    @NotEmpty(message = "Mail subject required")
    private String subject;

    private Map<String, String> props = new HashMap<>();

    private List<Object> attachments;

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }

}
