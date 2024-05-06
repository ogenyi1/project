package ng.optisoft.rosabon.controller;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.StageInDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class StageController {

    @Autowired
    private StageService stageService;

    @PreAuthorize("hasAnyAuthority('CREATE_STAGE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.STAGE)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpResponseDto save(@Valid @RequestBody StageInDto dto) {
        return stageService.save(dto);
    }

    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.STAGE + "/{id}")
    public HttpResponseDto delete(@Valid @NotNull @PathVariable("id") Long id) {
        return stageService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_STAGE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.STAGE + "/{id}")
    public HttpResponseDto update(@Valid @NotNull @PathVariable("id") Long id,
                                  @Valid @RequestBody StageInDto dto) {
        return stageService.update(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_STAGE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.STAGE + "/{id}")
    public HttpResponseDto getById(@Valid @NotNull @PathVariable("id") Long id) {
        return stageService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_STAGE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.STAGE)
    public HttpResponseDto getAll(@RequestParam("platform") GenericModuleBaseEntity.Platform platform) {
        return stageService.getAll(platform);
    }

}
