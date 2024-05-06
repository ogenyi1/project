package ng.optisoft.rosabon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ng.optisoft.rosabon.constant.ResourceExtension;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {

    private String encodedString;

    private ResourceExtension resourceExtension;
}
