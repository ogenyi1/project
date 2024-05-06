package ng.optisoft.rosabon.controller;

import javax.validation.Valid;

import ng.optisoft.rosabon.dto.request.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.mapper.CompanyMapper;
import ng.optisoft.rosabon.model.Company;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.CompanyMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class CompanyController
{
	@Autowired
	private CompanyMngr service;
	
	private UseraccountDao useraccountDao;
	private ModelMapper mapper;
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY)
	public ResponseEntity<?> updateCompanyDetails(Authentication auth, @RequestBody @Valid UpdatableCompanyInfoInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		Company company = service.updateCompanyInformation(useraccount, request);
		
		return ResponseEntity.ok(CompanyMapper.mapToDto(company, mapper));
	}

	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.CREDIT)
	public ResponseEntity<?> updateCompanyDetailsCredit(Authentication auth, @RequestBody @Valid UpdateUserInDto request) {
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		Company company = service.update(request.getCompany(),useraccount);
		return ResponseEntity.ok(CompanyMapper.mapToDto(company, mapper));
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY)
	public ResponseEntity<?> createCompanyDetails(Authentication auth, @RequestBody @Valid CompanyInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		Company company = service.create(request, useraccount);
		
		return ResponseEntity.ok(CompanyMapper.mapToDto(company, mapper));
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.BUSINESS_DETAILS)
	public ResponseEntity<?> updateBusinessDetails(Authentication auth, @RequestBody @Valid BusinessDetailInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Updated successfully", service.updateBusinessDetail(useraccount, request)), HttpStatus.OK);
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.CONTACT_PERSON_DETAILS)
	public ResponseEntity<?> updateContactPersonDetails(Authentication auth, @RequestBody @Valid ContactPersonDetailInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Updated successfully", service.updateContactPersonDetails(useraccount, request)), HttpStatus.OK);
	}
}
