package ng.optisoft.rosabon.controller;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.FAQInDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FAQController {

    @Autowired
    private FAQService faqService;

    @PreAuthorize("hasAnyAuthority('CREATE_FAQ', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ)
    @ResponseStatus(HttpStatus.CREATED) //restarting the server2
    public HttpResponseDto save(Authentication auth, @Valid @RequestBody FAQInDto vo) {
        return faqService.save(vo);
    }

    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ + "/{id}")
    public HttpResponseDto delete(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return faqService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_FAQ', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FAQ + "/{id}")
    public HttpResponseDto update(Authentication auth, @Valid @NotNull @PathVariable("id") Long id,
                                  @Valid @RequestBody FAQInDto vo) {
        return faqService.update(id, vo);
    }
    
    @PreAuthorize("hasAnyAuthority('VIEW_FAQ', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.FAQ + "/{id}")
    public HttpResponseDto getById(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return faqService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_FAQ', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.FAQ)
    public HttpResponseDto getAll(Authentication auth,  @RequestParam("platform") GenericModuleBaseEntity.Platform platform,@RequestParam(value = "search",required = false)String search) {
        return faqService.getAll(platform, search);
    }

}
