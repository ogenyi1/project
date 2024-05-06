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
import ng.optisoft.rosabon.dto.request.GenderInDto;
import ng.optisoft.rosabon.dto.request.UpdateGenderRequest;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.model.Gender;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.GenderService;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class GenderController
{
	private GenderService service;
	private UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.GENDER)
	@PreAuthorize("hasAnyAuthority('CREATE_GENDER', 'SUPER_ADMINISTRATOR')")
	public ResponseEntity<?> createGender(Authentication auth, @RequestBody @Valid @NotNull GenderInDto request)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var gender = service.createGender(request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Gender Created Successfully", gender), HttpStatus.CREATED);
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.GENDER + "/{id}")
	@PreAuthorize("hasAnyAuthority('EDIT_GENDER', 'SUPER_ADMINISTRATOR')")
	public ResponseEntity<?> updateGender(Authentication auth, @RequestBody @NotNull UpdateGenderRequest request, @PathVariable Long id)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var gender = service.updateGender(id, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Gender Updated Successfully", gender), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.GENDER)
	@PreAuthorize("hasAnyAuthority('VIEW_GENDER', 'CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	public ResponseEntity<?> getAllGender(Authentication auth,
			@RequestParam(required = false) Gender.GenderStatus status,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		
		if(admin == null)
			throw new NotFoundException("admin not found!");
		
		var gender = service.getAllGenders(status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(gender);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_GENDER', 'CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.GENDER + "/{id}")
	public ResponseEntity<?> getGenderById(@PathVariable Long id)
	{
		return ResponseEntity.ok(service.getGenderById(id));
	}
}
