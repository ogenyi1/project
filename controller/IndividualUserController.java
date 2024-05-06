package ng.optisoft.rosabon.controller;

import javax.validation.Valid;

import ng.optisoft.rosabon.dto.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.response.IndividualUserDto;
import ng.optisoft.rosabon.dto.response.SmsVerificationResponse;
import ng.optisoft.rosabon.mapper.IndividualUserMapper;
import ng.optisoft.rosabon.model.IndividualUser;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.IndividualUserMngr;
import ng.optisoft.rosabon.service.OtpService;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin("*")
public class IndividualUserController
{
	@Autowired
	private IndividualUserMngr service;
	@Autowired
	private UseraccountDao useraccountDao;
	@Autowired
	private OtpService otpService;
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER)
	public ResponseEntity<?> updatePersonalInformation(Authentication auth, @RequestBody @Valid UpdateIndividualUserProfileInDto request) {
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		IndividualUser individualUser = service.updatePersonalInformation(useraccount, request);
		return ResponseEntity.ok(IndividualUserMapper.mapToDto(individualUser));
	}

	@PutMapping(value=ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.CREDIT)
	public ResponseEntity<?> updatePersonalInformationCredit (Authentication auth, @RequestBody @Valid UpdateIndividualUserInDto request) {
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());

		IndividualUser individualUser = service.update(request,useraccount);

		return ResponseEntity.ok(IndividualUserMapper.mapToDto(individualUser));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.SEND_OTP)
	public ResponseEntity<?> sendOtpToIndividualUser(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		String otp = service.sendOtp(useraccount);
		
		return new ResponseEntity<>(new ResponseHelper(true, "An OTP has been sent to your registered email address, the OTP will expire after 10 mins", otp), HttpStatus.OK);
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER)
	public ResponseEntity<?> createIndividualUser(Authentication auth, @RequestBody @Valid IndividualUserInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		IndividualUser individualUser = service.create(request, useraccount);
		
		return ResponseEntity.ok(IndividualUserMapper.mapToDto(individualUser));
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.VERIFY_PHONE)
	public ResponseEntity<?> verifyPhoneNumber(Authentication auth, @RequestParam String recipient)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		SmsVerificationRequest request = new SmsVerificationRequest();
		
//		if(otpService.getOtp(auth.getName()) != null)
//			otpService.clearOTP(auth.getName());
		
		String otp = otpService.generateOTP(auth.getName());
		
		String message = "Here is your OTP: " + otp + "\nPlease do not share with anyone";
		
		request.setRecipient(recipient);
		request.setMessage(message);
		
		SmsVerificationResponse response = service.verifyPhoneNumber(useraccount, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "An otp has been sent to your this number. It will expire in 10 mins", response), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.VALIDATE_PHONE)
	public ResponseEntity<?> validatePhoneNumberOtp(Authentication auth, @RequestParam String otp)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		IndividualUserDto response = service.validatePhoneNumberOtp(user, otp);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Phone number validated successfully", response), HttpStatus.OK);
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.CONTACT_DETAIL)
	public ResponseEntity<?> editContactDetail(Authentication auth, @RequestBody @Valid UpdateContactDetailInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		IndividualUserDto individualUser = service.editContactDetail(useraccount, request);
		
		return ResponseEntity.ok(individualUser);
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.VALIDATE_OTP + "/{otp}")
	public ResponseEntity<?> validateOtp(Authentication auth, @PathVariable String otp)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.validateOtp(useraccount, otp));
	}
}
