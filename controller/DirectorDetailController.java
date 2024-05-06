package ng.optisoft.rosabon.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.CreditDirectorDetailInDto;
import ng.optisoft.rosabon.dto.request.DirectorDetailInDto;
import ng.optisoft.rosabon.dto.response.CreditDirectorDetailDto;
import ng.optisoft.rosabon.dto.response.DirectorDetailDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.DirectorDetailMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDetailController
{
	@Autowired
	private UseraccountDao useraccountDao;
	
	@Autowired
	private DirectorDetailMngr service;
	
	@Value("${verify-bvn-url}")
	private String bvnUrl;
	
	@Value("${base-url}")
	private String baseUrl;
	
	@Value("${youverify-api-key}")
	private String youverifyToken;
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS)
	public ResponseEntity<?> updateDirectorDetail(Authentication auth, @RequestBody List<@Valid DirectorDetailInDto> requests)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		ResponseHelper response = service.updateDirectorDetails(user, requests);
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + ApiRoute.CREDIT)
	public ResponseEntity<?> updateDirectorDetail_Credit(Authentication auth, @RequestBody List<@Valid CreditDirectorDetailInDto> requests)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		ResponseHelper response = service.updateDirectorDetails_Credit(user, requests);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + ApiRoute.SEND_OTP)
	public ResponseEntity<?> sendOtpToCompany(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		String otp = service.sendOtp(useraccount);
		
		return new ResponseEntity<>(new ResponseHelper(true, "An OTP has been sent to your email address, It will expire in 10mins", otp), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS)
	public ResponseEntity<?> getDirectorDetailsByCompany(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<DirectorDetailDto> directorDetails = service.getDirectorDetailsByCompany(user);
		
		return ResponseEntity.ok(directorDetails);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + ApiRoute.CREDIT)
	public ResponseEntity<?> getDirectorDetailsByCompany_Credit(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<CreditDirectorDetailDto> directorDetails = service.getDirectorDetailsByCompany_Credit(user);
		
		return ResponseEntity.ok(directorDetails);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + "/{id}")
	public ResponseEntity<?> getDirectorDetailsById(Authentication auth, @PathVariable Long id)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		DirectorDetailDto directorDetail = service.getDirectorDetailsById(user, id);
		
		return ResponseEntity.ok(directorDetail);
	}
	
	@DeleteMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + "/{id}")
	public ResponseEntity<?> deleteDirectorDetailsById(Authentication auth, @PathVariable Long id)
	{
//		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
//		DirectorDetailDto directorDetail = 
		
		return new ResponseEntity<>(service.deleteDirectorDetailsById(id), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + ApiRoute.CREDIT + "/{id}")
	public ResponseEntity<?> getDirectorDetailsById_Credit(Authentication auth, @PathVariable Long id)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		CreditDirectorDetailDto directorDetail = service.getDirectorDetailsById_Credit(user, id);
		
		return ResponseEntity.ok(directorDetail);
	}
}
