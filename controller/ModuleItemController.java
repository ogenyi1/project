package ng.optisoft.rosabon.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.ModuleItemInDto;
import ng.optisoft.rosabon.dto.response.ModuleItemDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.ModuleItemMngr;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ModuleItemController {

	private ModuleItemMngr service;
	private UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE_ITEM)
    public ResponseEntity<?> createModuleItem(@RequestBody @Valid ModuleItemInDto request, @RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform)
    {    	    	
    	return new ResponseEntity<>(service.createModuleItem(request, platform), HttpStatus.CREATED);
    }
    
//    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE_ITEM + "/{id}")
//    public ResponseEntity<?> editModuleItem(Authentication auth, @RequestBody @Valid UpdateModuleItemRequest request, @PathVariable Long id)
//    {    	
//    	ModuleItemDto module = service.editModuleItem(id, request);
//    	
//    	return new ResponseEntity<>(new ResponseHelper(true, "Module edited successfully", module), HttpStatus.OK);
//    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE_ITEM)
    public ResponseEntity<?> getAllModuleItems(@RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform)
    {    	
    	List<ModuleItemDto> moduleItems = service.getAllModuleItems(platform);
    	
    	return ResponseEntity.ok(moduleItems);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.ELIGIBLE_MODULE_ITEMS)
    public ResponseEntity<?> getAllEligibleModuleItems(Authentication auth)
    {
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	Set<ModuleItemDto> moduleItems = service.getAllEligibleModuleItems(user);
    	
    	return ResponseEntity.ok(moduleItems);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE_ITEM + "/{id}")
    public ResponseEntity<?> getModuleItemById(Authentication auth, @PathVariable Long id)
    {    	
    	ModuleItemDto moduleItem = service.getModuleItemById(id);
    	
    	return ResponseEntity.ok(moduleItem);
    }
}
