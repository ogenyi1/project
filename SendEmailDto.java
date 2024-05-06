package ng.optisoft.rosabon.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SendEmailDto {
    private String from;

    private String mailTo;

    private String subject;

    private Map<String, String> props = new HashMap<>();

    private String body;

    private List<Object> attachments;
}
