package ng.optisoft.rosabon.event;

import lombok.Getter;
import lombok.Setter;
import ng.optisoft.rosabon.dto.response.CreateAuditTrailDto;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AuditTrailEvent extends ApplicationEvent {

    private CreateAuditTrailDto createAuditTrailDto;

    public AuditTrailEvent(Object source) {
        super(source);
    }

    public AuditTrailEvent(Object src, CreateAuditTrailDto createAuditTrailDto) {
        super(src);
        this.createAuditTrailDto = createAuditTrailDto;
    }
}
