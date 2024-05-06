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
import ng.optisoft.rosabon.dto.request.DepartmentInDto;
import ng.optisoft.rosabon.dto.request.UpdateDepartmentRequest;
import ng.optisoft.rosabon.model.Department;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.DepartmentMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class DepartmentController
{
	private DepartmentMngr service;
	private UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('CREATE_DEPARTMENT', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.DEPARTMENTS)
	public ResponseEntity<?> createDepartment(Authentication auth, @RequestBody @Valid DepartmentInDto request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Department Created Successfully", service.createDepartment(user, request)), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_DEPARTMENT', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.DEPARTMENTS)
	public ResponseEntity<?> getAllDepartments(Authentication auth,
			@RequestParam(required = false) Department.Status status,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		return ResponseEntity.ok(service.getAllDepartments(status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to)));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_DEPARTMENT', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.DEPARTMENTS + "/{id}")
	public ResponseEntity<?> getDepartmentById(Authentication auth, @PathVariable Long id)
	{
		return ResponseEntity.ok(service.getDeparmentById(id));
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_DEPARTMENT', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.DEPARTMENTS + "/{id}")
	public ResponseEntity<?> updateDepartment(Authentication auth, @RequestBody UpdateDepartmentRequest request, @PathVariable Long id)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Department Updated Successfully", service.editDepartment(user, request, id)), HttpStatus.OK);
	}
}
