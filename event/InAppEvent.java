package ng.optisoft.rosabon.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.optisoft.rosabon.constant.MessageConstant;
import ng.optisoft.rosabon.enums.NotificationCategory;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.model.InApp;
import ng.optisoft.rosabon.validator.ValueOfEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InAppEvent {

    private Long id;

    @NotNull(message = "make sure you entered the right key *message* , and the value must not be null")
    @NotBlank(message = "message cannot be blank, and make sure you use the right key *message*")
    private String message;

    @NotBlank(message = "recipient user id must not be blank, also enter the right key *userId*")
    private Long recipientUserId;

    @NotNull(message = "make sure you entered the right key *initiator* , and the value must not be null")
    @NotBlank(message = "initiator field must not be blank, and make sure you use the right key *initiator*")
    private Long initiatorUserId;

    @NotBlank(message = "notification category can not be blank, also enter the right key *userId*")
    @NotEmpty(message = MessageConstant.PLATFORM_EMPTY)
    @ValueOfEnum(enumClass = NotificationCategory.class, message = MessageConstant.WRONG_PLATFORM)
    private NotificationCategory category;

    @NotNull(message = "Please enter platform")
    @ValueOfEnum(enumClass = GenericModuleBaseEntity.Platform.class, message = MessageConstant.WRONG_PLATFORM)
    private GenericModuleBaseEntity.Platform platform;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateSent;

    @Enumerated(EnumType.STRING)
    private InApp.Status readStatus;
}
