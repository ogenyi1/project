package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.RoleManagerInDto;
import ng.optisoft.rosabon.dto.request.UpdateRoleManagerRequest;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.RoleManagerMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class RoleManagerController
{
	private RoleManagerMngr service;
	private UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('CREATE_ROLE', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.ROLE)
	public ResponseEntity<?> createRole(
			@RequestBody @Valid RoleManagerInDto request,
			@RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform)
	{
		return new ResponseEntity<>(service.createRole(request, platform), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_ROLE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER', 'CREATE_PROCESS_FLOW', 'EDIT_STAGE',' EDIT_PROCESS_FLOW','CREATE_STAGE')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.ROLE)
	public ResponseEntity<?> getAllRoles(
			@RequestParam(defaultValue = "TREASURY") GenericModuleBaseEntity.Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		return ResponseEntity.ok(service.getAllRoles(search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to), platform));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_ROLE', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER'," +
			" 'EDIT_STAGE',' EDIT_PROCESS_FLOW','CREATE_STAGE')")//added by Johnny
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.ROLE + "/{id}")
	public ResponseEntity<?> getRoleById(Authentication auth, @PathVariable Long id)
	{
		return ResponseEntity.ok(service.getRoleManagerById(id));
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_ROLE', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.ROLE + "/{id}")
	public ResponseEntity<?> updateRole(
			Authentication auth,
			@RequestBody @Valid UpdateRoleManagerRequest request,
			@PathVariable Long id,
			@RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Role Updated Successfully", service.editRole(user, request, id, platform)), HttpStatus.OK);
	}
	
//	@PatchMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.ASSIGN_ROLE)
//	public ResponseEntity<?> assignRoleToUsers(Authentication auth, @RequestBody @Valid AssignRoleRequest request)
//	{
//		Useraccount user = useraccountDao.findByEmail(auth.getName());
//		
//		return new ResponseEntity<>(new ResponseHelper(true, "Role Assigned Successfully", service.assignRoleToUsers(user, request)), HttpStatus.CREATED);
//	}
}
