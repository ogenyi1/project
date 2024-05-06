package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.TicketCategoryInDto;
import ng.optisoft.rosabon.dto.request.UpdateTicketCategoryRequest;
import ng.optisoft.rosabon.dto.response.TicketCategoryDto;
import ng.optisoft.rosabon.mapper.TicketCategoryMapper;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity.Platform;
import ng.optisoft.rosabon.model.TicketCategory;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.TicketCategoryMngr;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class TicketCategoryController {
    @Autowired
    private TicketCategoryMngr service;
    private UseraccountDao useraccountDao;
    private ModelMapper mapper;

    @PreAuthorize("hasAnyAuthority('CREATE_TICKET_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.CREATE_CATEGORY)
    public ResponseEntity<?> createTicketCategory(Authentication auth, @RequestBody @Valid TicketCategoryInDto dto) {
        Useraccount user = useraccountDao.findByEmail(auth.getName());

        TicketCategory category = service.create(dto, user);

        TicketCategoryDto response = TicketCategoryMapper.mapToDto(category, mapper);

        return new ResponseEntity<>(new ResponseHelper(true, "Ticket Category Created Successfully", response), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_TICKET_CATEGORY', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
//    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.CATEGORIES)
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.CATEGORIES)
    public ResponseEntity<?> getAllTicketCategory(Authentication auth, @RequestParam GenericModuleBaseEntity.Platform platform, @RequestParam(required = false) TicketCategory.Status status, @RequestParam(required = false) String search) {
        Useraccount user = useraccountDao.findByEmail(auth.getName());

        List<TicketCategoryDto> categories = service.getAllTicketCategory(user, status, platform,search);

        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasAnyAuthority('EDIT_TICKET_CATEGORY', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.EDIT_CATEGORY)
    public ResponseEntity<?> editTicketCategory(Authentication auth, @RequestBody @Valid UpdateTicketCategoryRequest dto, @RequestParam Platform platform) {
        Useraccount user = useraccountDao.findByEmail(auth.getName());

        TicketCategory category = service.editTicketCategory(dto, user, platform);

        TicketCategoryDto response = TicketCategoryMapper.mapToDto(category, mapper);

        return new ResponseEntity<>(new ResponseHelper(true, "Ticket Category edited Successfully", response), HttpStatus.OK);
    }
}
