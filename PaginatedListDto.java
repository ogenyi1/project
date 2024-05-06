package ng.optisoft.rosabon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedListDto<T> {

	private Integer page;

	private Integer limit;

	private Long total;

	private List<T> entities;

}
