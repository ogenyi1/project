package ng.optisoft.rosabon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.MyDepositService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MyDepositController
{
	private final MyDepositService myDepositService;
	private final UseraccountDao useraccountDao;
	
	@GetMapping(ApiRoute.AUTH + ApiRoute.MY_DEPOSITS)
	public ResponseEntity<?> getAllMyDeposits(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(myDepositService.getMyDeposits(user));
	}
	
	@GetMapping(ApiRoute.AUTH + ApiRoute.MY_DEPOSITS + "/{id}")
	public ResponseEntity<?> getMyDepositById(@PathVariable Long id)
	{
		return ResponseEntity.ok(myDepositService.getMyDepositById(id));
	}
	
	@GetMapping(ApiRoute.AUTH + ApiRoute.MY_DEPOSITS + ApiRoute.ACTIVITIES)
	public ResponseEntity<?> getAllMyDepositActivities(Authentication auth, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(myDepositService.getAllMyDepositActivities(user, page, limit));
	}
	
	@GetMapping(ApiRoute.AUTH + ApiRoute.MY_DEPOSITS + ApiRoute.ACTIVITIES + "/{id}")
	public ResponseEntity<?> getMyDepositActivityById(@PathVariable Long id)
	{
		return ResponseEntity.ok(myDepositService.getMyDepositActivityById(id));
	}
}
