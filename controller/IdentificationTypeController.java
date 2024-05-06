package ng.optisoft.rosabon.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.IdentificationTypeInDto;
import ng.optisoft.rosabon.dto.request.UpdateIdentificationTypeRequest;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.model.IdentificationType;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.IdentificationTypeMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class IdentificationTypeController
{
	private IdentificationTypeMngr service;
	private UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.IDENTIFICATION_TYPE)
	@PreAuthorize("hasAnyAuthority('CREATE_IDENTIFICATION_TYPE', 'SUPER_ADMINISTRATOR')")
	public ResponseEntity<?> createIdentificationType(Authentication auth, @RequestBody @Valid @NotNull IdentificationTypeInDto request)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var identificationType = service.createIndentificationType(request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Identification Type Created Successfully", identificationType), HttpStatus.CREATED);
	}
	
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.IDENTIFICATION_TYPE + "/{id}")
	@PreAuthorize("hasAnyAuthority('EDIT_IDENTIFICATION_TYPE', 'SUPER_ADMINISTRATOR')")
	public ResponseEntity<?> updateIdentificationType(Authentication auth, @RequestBody @NotNull UpdateIdentificationTypeRequest request, @PathVariable Long id)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var identificationType = service.updateIdentificationType(id, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Identification Type Updated Successfully", identificationType), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_IDENTIFICATION_TYPE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.IDENTIFICATION_TYPE + "/{id}")
	public ResponseEntity<?> getIdentificationTypeById(@PathVariable Long id)
	{
		return ResponseEntity.ok(service.getIdentificationType(id));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.IDENTIFICATION_TYPE)
	@PreAuthorize("hasAnyAuthority('VIEW_IDENTIFICATION_TYPE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	public ResponseEntity<?> getAllIdentificationType(Authentication auth,
			@RequestParam(required = false) IdentificationType.Status status,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var identificationType = service.getAllIdentificationType(status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(identificationType);
	}
}
