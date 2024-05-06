package ng.optisoft.rosabon.controller;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.ProcessFlowInDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.ProcessFlowService;
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
public class ProcessFlowController {

    final
    ProcessFlowService processFlowService;

    public ProcessFlowController(ProcessFlowService processFlowService) {
        this.processFlowService = processFlowService;
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PROCESS_FLOW', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.PROCESS)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpResponseDto save(@Valid @RequestBody ProcessFlowInDto vo) {
        return processFlowService.save(vo);
    }

    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.PROCESS + "/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        processFlowService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_PROCESS_FLOW', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.PROCESS + "/{id}")
    public HttpResponseDto update(@Valid @NotNull @PathVariable("id") Long id,
                                  @Valid @RequestBody ProcessFlowInDto dto) {
        return processFlowService.update(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_PROCESS_FLOW', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.PROCESS + "/{id}")
    public HttpResponseDto getById(@Valid @NotNull @PathVariable("id") Long id) {
        return processFlowService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_PROCESS_FLOW', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.PROCESS)
    public HttpResponseDto getAll(@RequestParam("platform") GenericModuleBaseEntity.Platform platform) {
        return processFlowService.getAll(platform);
    }

}
