package ng.optisoft.rosabon.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.UpdateMyCreditDocumentInDto;
import ng.optisoft.rosabon.dto.request.UpdateMyDocumentInDto;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.mapper.MyDocumentMapper;
import ng.optisoft.rosabon.model.MyDocument;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.model.Useraccount.Usage;
import ng.optisoft.rosabon.service.MyDocumentMngr;

@RestController
@CrossOrigin("*")
public class MyDocumentController
{
	@Autowired
	private UseraccountDao useraccountDao;
	
	@Autowired
	private MyDocumentMngr service;
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT)
	public ResponseEntity<?> updateMyDocument(Authentication auth, @RequestBody @Valid UpdateMyDocumentInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		MyDocument myDocument = service.updateMyDocument(useraccount, request);
		
		return ResponseEntity.ok(MyDocumentMapper.mapToDto(myDocument));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT)
	public ResponseEntity<?> getAllDocuments(@RequestParam(required = false, defaultValue = "TREASURY") Usage usage)
	{
		return ResponseEntity.ok(MyDocumentMapper.mapToList(service.getAllDocuments(usage)));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT + ApiRoute.GET_BY_USER)
	public ResponseEntity<?> getMyDocument(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		if(user == null)
			throw new NotFoundException("User not found!");
		
		return ResponseEntity.ok(MyDocumentMapper.mapToDto(service.getMyDocument(user)));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT + ApiRoute.GET_BY_USER + ApiRoute.CREDIT)
	public ResponseEntity<?> getMyDocumentCredit(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		if(user == null)
			throw new NotFoundException("User not found!");

		return ResponseEntity.ok(MyDocumentMapper.mapToCreditDocument(service.getMyDocument(user)));
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT + ApiRoute.CREDIT)
	public ResponseEntity<?> updateMyDocument(Authentication auth, @RequestBody @Valid UpdateMyCreditDocumentInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		MyDocument myDocument = service.updateMyCreditDocument(useraccount, request);
		
		return ResponseEntity.ok(MyDocumentMapper.mapToCreditDocument(myDocument));
	}
	
	@DeleteMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT + "/{myDocumentId}")
	public ResponseEntity<?> deleteMyDocument(@PathVariable Long myDocumentId) {
		service.deleteById(myDocumentId);
		return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.MY_DOCUMENT + ApiRoute.SEND_OTP)
	public ResponseEntity<?> sendOtpToUser(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		service.sendOtp(useraccount);
		
		return ResponseEntity.ok("An OTP has been sent to your registered email address, the OTP will expire after 10 mins");
	}
}
