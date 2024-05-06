package ng.optisoft.rosabon.event;

import lombok.Data;
import ng.optisoft.rosabon.dto.response.CreateNotificationDto;
import org.springframework.context.ApplicationEvent;

@Data
public class NotificationEvent extends ApplicationEvent {

    private CreateNotificationDto createNotificationDto;

    public NotificationEvent(Object source) {
        super(source);
    }

    public NotificationEvent(Object src, CreateNotificationDto createNotificationDto) {
        super(src);
        this.createNotificationDto = createNotificationDto;
    }
}
