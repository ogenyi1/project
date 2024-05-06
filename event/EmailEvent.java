package ng.optisoft.rosabon.event;

import lombok.Getter;
import lombok.Setter;
import ng.optisoft.rosabon.dto.EmailTemplateDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.util.ToStringUtil;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmailEvent extends ApplicationEvent {

    private EmailTemplateDto emailTemplateDto;
    private GenericModuleBaseEntity.Platform platform;

    public EmailEvent(Object source) {
        super(source);
    }

    public EmailEvent(Object src, EmailTemplateDto emailTemplateDto, GenericModuleBaseEntity.Platform platform) {
        super(src);
        this.emailTemplateDto = emailTemplateDto;
        this.platform = platform;
    }

    @Override
    public String toString() {
        return ToStringUtil.print(this);
    }
}
