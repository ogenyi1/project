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
import ng.optisoft.rosabon.dto.request.CreditNokDetailInDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.NextOfKinDetailMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class NextOfKinDetailController
{
	private UseraccountDao useraccountDao;
	private NextOfKinDetailMngr service;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.NOK_DETAIL)
	public ResponseEntity<?> updateNokDetail(Authentication auth, @RequestBody CreditNokDetailInDto dto)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new ResponseHelper(true, "Nok Detail Updated Successfully", service.updateCreditNokDetail(user, dto)), HttpStatus.OK);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.NOK_DETAIL + ApiRoute.GET_BY_USER)
	public ResponseEntity<?> getByUser(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getNokDetailByUser(user));
	}
}
