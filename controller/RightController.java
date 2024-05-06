package ng.optisoft.rosabon.controller;

import java.util.List;

import javax.validation.Valid;

import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.RightInDto;
import ng.optisoft.rosabon.dto.request.UpdateModuleItemRequest;
import ng.optisoft.rosabon.dto.response.RightDto;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.RightMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class RightController
{
	private RightMngr service;
	private UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.RIGHTS)
    public ResponseEntity<?> createRight(@RequestBody @Valid RightInDto request, @RequestParam(defaultValue = "TREASURY") GenericModuleBaseEntity.Platform platform)
    {    	    	
    	return new ResponseEntity<>(service.createRight(request, platform), HttpStatus.CREATED);
    }
    
    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.RIGHTS + "/{id}")
    public ResponseEntity<?> editRight(Authentication auth, @RequestBody @Valid UpdateModuleItemRequest request, @PathVariable Long id)
    {    	
    	RightDto moduleItem = service.editRight(id, request);
    	
    	return new ResponseEntity<>(new ResponseHelper(true, "Right edited successfully", moduleItem), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.RIGHTS + "/{moduleItemName}")
    public ResponseEntity<?> getAllRightsByModuleItem(@RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform, @PathVariable String moduleItemName)
    {    	
    	List<RightDto> moduleItems = service.getAllRightsByModuleItem(moduleItemName, platform);
    	
    	return ResponseEntity.ok(moduleItems);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.RIGHTS)
    public ResponseEntity<?> getAllRights(@RequestParam(defaultValue = "TREASURY") GenericModuleBaseEntity.Platform platform)
    {    	
    	List<RightDto> rights = service.getAllRights(platform);
    	
    	return ResponseEntity.ok(rights);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.RIGHTS + ApiRoute.USER)
    public ResponseEntity<?> getAllRightsByUser(Authentication auth)
    {    	
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	if(user == null)
    		throw new BadRequestException("User details not found");
    	List<RightDto> rights = service.getAllRightsByUser(user);
    	return ResponseEntity.ok(rights);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.RIGHTS + "/{id}")
    public ResponseEntity<?> getRightById(Authentication auth, @PathVariable Long id)
    {    	
    	RightDto right = service.getRightById(id);
    	
    	return ResponseEntity.ok(right);
    }
}
