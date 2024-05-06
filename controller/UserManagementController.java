package ng.optisoft.rosabon.controller;

import java.util.List;

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
import ng.optisoft.rosabon.dto.request.AssignRoleRequest;
import ng.optisoft.rosabon.dto.request.UpdateUserRequest;
import ng.optisoft.rosabon.dto.request.UserManagementInDto;
import ng.optisoft.rosabon.dto.response.UserManagementDto;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity.Platform;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.UserManagementMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class UserManagementController
{
	private UserManagementMngr service;
    private UseraccountDao useraccountDao;
    
    @PreAuthorize("hasAnyAuthority('CREATE_ADMIN_USER', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USERS)
    public ResponseEntity<?> createUser(Authentication auth, @RequestBody @Valid UserManagementInDto request)
    {
    	Useraccount adminUser = useraccountDao.findByEmail(auth.getName());
    	
    	UserManagementDto userDto = service.createUser(adminUser, request);
    	
    	return new ResponseEntity<>(new ResponseHelper(true, "User created successfully", userDto), HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasAnyAuthority('EDIT_ADMIN_USER', 'SUPER_ADMINISTRATOR')")
    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USERS + "/{userId}")
    public ResponseEntity<?> editUser(Authentication auth, @RequestBody @Valid UpdateUserRequest request, @PathVariable Long userId)
    {
    	Useraccount adminUser = useraccountDao.findByEmail(auth.getName());
    	
    	UserManagementDto userDto = service.editUser(adminUser, request, userId);
    	
    	return new ResponseEntity<>(new ResponseHelper(true, "User edited successfully", userDto), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('VIEW_ADMIN_USER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USERS)
    public ResponseEntity<?> getAllUsers(Authentication auth,
    		@RequestParam(required = false) Useraccount.Status status,
    		@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to,
            @RequestParam(defaultValue = "TREASURY") Platform platform,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit)
    {
    	Useraccount adminUser = useraccountDao.findByEmail(auth.getName());
    	List<UserManagementDto> userDtos = service.getAllUsers(platform, status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to), page, limit);
    	return ResponseEntity.ok(userDtos);
    }
    
    @PreAuthorize("hasAnyAuthority('VIEW_ADMIN_USER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USERS + "/{userId}")
    public ResponseEntity<?> getUserById(Authentication auth, @PathVariable Long userId)
    {    	
    	UserManagementDto userDto = service.getUserById(userId);
    	
    	return ResponseEntity.ok(userDto);
    }
    
    @PreAuthorize("hasAnyAuthority('CREATE_ROLE', 'SUPER_ADMINISTRATOR')")
    @PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USERS + ApiRoute.ASSIGN_ROLE)
    public ResponseEntity<?> assignRoleToUser(@RequestBody @Valid AssignRoleRequest request)
    {    	
    	return new ResponseEntity<>(service.assignRoleToUser(request), HttpStatus.CREATED);
    }
    
}
