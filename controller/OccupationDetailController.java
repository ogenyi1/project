package ng.optisoft.rosabon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.OccupationDetailInDto;
import ng.optisoft.rosabon.dto.response.OccupationDetailDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.OccupationDetailMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class OccupationDetailController
{
	private UseraccountDao useraccountDao;
	private OccupationDetailMngr service;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.OCCUPATION_DETAILS)
	public ResponseEntity<?> updateOccupationDetail(Authentication auth, @RequestBody OccupationDetailInDto request)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		OccupationDetailDto dto = service.updateOccupationDetail(useraccount, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Updated Successfully", dto), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.OCCUPATION_DETAILS + ApiRoute.GET_BY_USER)
	public ResponseEntity<?> getByUser(Authentication auth)
	{
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());
		
		OccupationDetailDto dto = service.getByUser(useraccount);
		
		return ResponseEntity.ok(dto);
	}
}
