package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.service.CrGroupCompanyMgtService;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CreditGroupCompanyController {
    private final CrGroupCompanyMgtService crGroupCompanyMgtService;
    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_GROUP_COMPANY)
    public ResponseEntity<?> getAllGroupCompany(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(crGroupCompanyMgtService.getAllGroupCompany(pageable));
    }

    @GetMapping(ApiRoute.USERS + ApiRoute.GROUP_COMPANY + "/{id}")
    public HttpResponseDto getGroupCompanyById(@Valid @NotNull @PathVariable("id") Long id) {
        return crGroupCompanyMgtService.getById(id);
    }
}
