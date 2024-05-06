package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.PayoutRequest;
import ng.optisoft.rosabon.dto.request.SpecialEarningRequest;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.SpecialEarningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class SpecialEarningController
{
	private final SpecialEarningService service;
	private final UseraccountDao useraccountDao;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS)
	public ResponseEntity<?> createSpecialEarning(Authentication auth, @RequestBody @NotNull @Valid SpecialEarningRequest request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createSpecialEarning(user, request));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + "/{id}")
	public ResponseEntity<?> getAllSpecialEarnings(Authentication auth, @PathVariable Long id)
	{		
		return ResponseEntity.ok(service.getSpecialEarning(id));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.ACTIVITIES)
	public ResponseEntity<?> getAllSpecialEarningActivity(Authentication auth, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getAllSpecialEarningActivity(user, page, limit));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.ACTIVITIES + "/{id}")
	public ResponseEntity<?> getSpecialEarning(Long id)
	{
		return ResponseEntity.ok(service.getSpecialEarning(id));
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.REDEEM_BONUS)
	public ResponseEntity<?> redeemEarning(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.redeemEarning(user));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_WALLET_MANAGEMENT', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.UPLOAD_SCHEDULE_PAYOUT)
	public ResponseEntity<?> uploadSchedulePayout(Authentication auth, @RequestBody PayoutRequest request)
	{		
		String base64EncodedUpload = request.getBase64encodedUpload();
		String fileName = request.getFileName();
		
		return ResponseEntity.ok(service.uploadSchedulePayout(base64EncodedUpload, fileName));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.PAYOUT_ACTIVITIES)
	public ResponseEntity<?> getAllPayoutActivity()
	{
		return ResponseEntity.ok(service.getAllPayoutActivity());
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.PAYOUT_ACTIVITIES + ApiRoute.USER)
	public ResponseEntity<?> getAllPayoutActivityByUser(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getAllPayoutActivityByUser(user));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.PAYOUT_ACTIVITIES + "/{id}")
	public ResponseEntity<?> getPayoutActivityById(@PathVariable Long id)
	{
		return ResponseEntity.ok(service.getPayoutActivityById(id));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.TOTAL_REDEEMED_EARNING)
	public ResponseEntity<?> getTotalRedeemedEarning(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getTotalRedeemedEarning(user));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.SPECIAL_EARNINGS + ApiRoute.TOTAL_EARNING)
	public ResponseEntity<?> getTotalEarning(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getTotalEarning(user));
	}
}
