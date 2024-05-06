package ng.optisoft.rosabon.controller;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.UpdateCompanyDocumentInDto;
import ng.optisoft.rosabon.dto.request.UpdateCompanyDocumentInDto2;
import ng.optisoft.rosabon.dto.response.CompanyDocumentDto;
import ng.optisoft.rosabon.mapper.CompanyDocumentMapper;
import ng.optisoft.rosabon.model.CompanyDocument;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.CompanyDocumentMngr;
import ng.optisoft.rosabon.service.OtpService;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class CompanyDocumentController
{
	@Autowired
	private UseraccountDao useraccountDao;
	
	@Autowired
	private CompanyDocumentMngr service;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private ModelMapper mapper;
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT)
	public ResponseEntity<?> updateCompanyDocument(Authentication auth, @RequestBody @Valid UpdateCompanyDocumentInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		CompanyDocument companyDocument = service.updateCompanyDocument(useraccount, request);
		
		return ResponseEntity.ok(CompanyDocumentMapper.mapToDto(companyDocument));
	}

	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT + ApiRoute.SEPARATOR + "two")
	public ResponseEntity<?> updateCompanyDocument2(Authentication auth, @RequestBody @Valid UpdateCompanyDocumentInDto2 request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());

		CompanyDocument companyDocument = service.updateCompanyDocument2(useraccount, request);

		return ResponseEntity.ok(CompanyDocumentMapper.mapToDto(companyDocument));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT + ApiRoute.SEND_OTP)
	public ResponseEntity<?> sendOtpToCompany(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		String otp = service.sendOtp(useraccount);
		
		return new ResponseEntity<>(new ResponseHelper(true, "An OTP has been sent to your registered email address, the OTP will expire after 10 mins", otp), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT)
	public ResponseEntity<?> getCompanyDocument(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		CompanyDocumentDto companyDocument = service.getCompanyDocument(useraccount);
		
		return ResponseEntity.ok(companyDocument);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT + "/{id}")
	public ResponseEntity<?> getCompanyDocument(@PathVariable Long id)
	{
//		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		CompanyDocumentDto companyDocument = service.getCompanyDocumentById(id);
		
		return ResponseEntity.ok(companyDocument);
	}
}
