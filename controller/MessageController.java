package ng.optisoft.rosabon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.MessageInDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.MessageService;
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
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class MessageController {


    private final MessageService messageService;

    @Operation(
            summary = "Send message to users",
            description = "To send message using ERP customer id, pass the ERP generated customer Id or list of ids if you are sending to multiple users, " +
                    "to the recipient field and pass 'recipientType' as null. To send to a specific set of users eg. all individual users, " +
                    "set recipient field as null and pass the ENUM of the user type eg. INDIVIDUAL_USER, COMPANY etc. " +
                    "Make sure to specify platform as TREASURY or CREDIT as the case may be"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto.class)))
    @PreAuthorize("hasAnyAuthority('CREATE_MESSAGE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MESSAGE_CENTRE)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpResponseDto save(Authentication auth, @Valid @RequestBody MessageInDto vo) {
        return messageService.save(auth, vo);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_MESSAGE', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MESSAGE_CENTRE + "/{id}")
    public HttpResponseDto delete(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return messageService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_MESSAGE', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MESSAGE_CENTRE + "/{id}")
    public HttpResponseDto update(Authentication auth, @Valid @NotNull @PathVariable("id") Long id,
                                  @Valid @RequestBody MessageInDto vo) {
        return messageService.update(id, vo);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_MESSAGE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.MESSAGE_CENTRE + "/{id}")
    public HttpResponseDto getById(Authentication auth, @Valid @NotNull @PathVariable("id") Long id) {
        return messageService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_MESSAGE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.MESSAGE_CENTRE)
    public HttpResponseDto getAll(Authentication auth, @RequestParam("platform") GenericModuleBaseEntity.Platform platform,
                                  @RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "sort", defaultValue = "dateAdded") String sort) {
        return messageService.getAll(platform, search,sort);
    }

}
