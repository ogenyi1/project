package ng.optisoft.rosabon.controller;

import javax.validation.Valid;

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
import ng.optisoft.rosabon.dto.request.SourceInDto;
import ng.optisoft.rosabon.dto.request.UpdateSourceRequest;
import ng.optisoft.rosabon.model.Source;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.SourceMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
//@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class SourceController
{
	private SourceMngr service;
	private UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('CREATE_SOURCE', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.SOURCES)
	public ResponseEntity<?> createSource(Authentication auth, @RequestBody @Valid SourceInDto request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Source created successfully", service.createSource(user, request)), HttpStatus.CREATED);
	}
	
//	@PreAuthorize("hasAnyAuthority('VIEW_SOURCE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.SOURCES)
	public ResponseEntity<?> getAllSources(Authentication auth,
			@RequestParam(required = false) Source.Status status,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		return ResponseEntity.ok(service.getAllSources(status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to)));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SOURCE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.SOURCES + "/{id}")
	public ResponseEntity<?> getSourceById(Authentication auth, @PathVariable Long id)
	{
		return ResponseEntity.ok(service.getById(id));
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_SOURCE', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.SOURCES + "/{id}")
	public ResponseEntity<?> updateSource(Authentication auth, @RequestBody @Valid UpdateSourceRequest request, @PathVariable Long id)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Source updated successfully", service.editSource(user, request, id)), HttpStatus.OK);
	}
}
