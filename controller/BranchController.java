package ng.optisoft.rosabon.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import ng.optisoft.rosabon.dto.request.BranchInDto;
import ng.optisoft.rosabon.dto.request.UpdateBranchRequest;
import ng.optisoft.rosabon.enums.EntityStatus;
import ng.optisoft.rosabon.model.Branch;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.BranchMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class BranchController
{
	private BranchMngr service;
	private UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('CREATE_BRANCH', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.BRANCHES)
	public ResponseEntity<?> createBranch(Authentication auth, @RequestBody @Valid BranchInDto request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Branch Created Successfully", service.createBranch(user, request)), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_BRANCH', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.BRANCHES)
	public ResponseEntity<?> getAllBranches(Authentication auth,
			@RequestParam(required = false) Branch.Status status,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to
			)
	{
		return ResponseEntity.ok(service.getAllBranches(status, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to)));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_BRANCH', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.BRANCHES + "/{id}")
	public ResponseEntity<?> getBranchById(Authentication auth, @PathVariable Long id)
	{
		return ResponseEntity.ok(service.getBranchById(id));
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_BRANCH', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.BRANCHES + "/{id}")
	public ResponseEntity<?> updateBranch(Authentication auth, @RequestBody @Valid UpdateBranchRequest request, @PathVariable Long id)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Branch Updated Successfully", service.editBranch(user, request, id)), HttpStatus.OK);
	}
}
