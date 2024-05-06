package ng.optisoft.rosabon.controller;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.FAQCategoryInDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.FAQCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class FAQCategoryController {

    private final FAQCategoryService trFAQService;

    public FAQCategoryController(FAQCategoryService trFAQService) {
        this.trFAQService = trFAQService;
    }

    @PreAuthorize("hasAnyAuthority('CREATE_FAQ_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ_CATEGORY)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpResponseDto save(Authentication auth, @Valid @RequestBody FAQCategoryInDto vo) {
        return trFAQService.save(vo);
    }

    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ_CATEGORY + "/{id}")
    public HttpResponseDto delete(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return trFAQService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_FAQ_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ_CATEGORY + "/{id}")
    public HttpResponseDto update(Authentication auth, @Valid @NotNull @PathVariable("id") Long id,
                                  @Valid @RequestBody FAQCategoryInDto vo) {
        return trFAQService.update(id, vo);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_FAQ_CATEGORY', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.FAQ_CATEGORY + "/{id}")
    public HttpResponseDto getById(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return trFAQService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_FAQ_CATEGORY', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.FAQ_CATEGORY)
    public HttpResponseDto getAll(Authentication auth, @RequestParam("platform") GenericModuleBaseEntity.Platform platform,@RequestParam(value = "search",required = false)String search) {
        return trFAQService.getAll(platform,search);
    }

}
