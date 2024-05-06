package ng.optisoft.rosabon.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.ModuleInDto;
import ng.optisoft.rosabon.dto.request.UpdateModuleRequest;
import ng.optisoft.rosabon.dto.response.ModuleDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.ModuleMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class ModuleController
{
	private ModuleMngr service;
	private UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE)
    public ResponseEntity<?> createModule(
            @RequestBody @Valid ModuleInDto request,
            @RequestParam(defaultValue = "TREASURY")GenericModuleBaseEntity.Platform platform)
    {    	    	
    	return new ResponseEntity<>(service.createModule(request, platform), HttpStatus.CREATED);
    }
    
    @PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE + "/{id}")
    public ResponseEntity<?> editModule(Authentication auth, @RequestBody @Valid UpdateModuleRequest request, @PathVariable Long id)
    {    	
    	ModuleDto module = service.editModule(id, request);
    	
    	return new ResponseEntity<>(new ResponseHelper(true, "Module edited successfully", module), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE)
    public ResponseEntity<?> getAllModules(@RequestParam(defaultValue = "TREASURY") GenericModuleBaseEntity.Platform platform)
    {    	
    	List<ModuleDto> modules = service.getAllModules(platform);
    	
    	return ResponseEntity.ok(modules);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.ELIGIBLE_MODULES)
    public ResponseEntity<?> getAllEligibleModules(Authentication auth)
    {
    	Useraccount user = useraccountDao.findByEmail(auth.getName());
    	
    	Set<ModuleDto> modules = service.getAllEligibleModules(user);
    	
    	return ResponseEntity.ok(modules);
    }
    
    @GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MODULE + "/{id}")
    public ResponseEntity<?> getModuleById(Authentication auth, @PathVariable Long id)
    {    	
    	ModuleDto module = service.getModuleById(id);
    	
    	return ResponseEntity.ok(module);
    }
}
