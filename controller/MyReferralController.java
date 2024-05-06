package ng.optisoft.rosabon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.MyReferralService;

@RestController
@CrossOrigin
@AllArgsConstructor
public class MyReferralController 
{
	private UseraccountDao useraccountDao;
	private MyReferralService service;
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS)
	public ResponseEntity<?> getMyReferrals(Authentication auth, @RequestParam(required=false, defaultValue="0") int page, @RequestParam(required=false, defaultValue="10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getMyReferrals(user, page, limit));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.IS_POKEABLE + "/{user-id}")
	public ResponseEntity<?> isPokeable(@PathVariable("user-id") Long userId) {
		return ResponseEntity.ok(service.isPokeable(userId));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.CREDIT)
	public ResponseEntity<?> getMyReferralsCredit(Authentication auth, @RequestParam(required=false, defaultValue="0") int page, @RequestParam(required=false, defaultValue="10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		return ResponseEntity.ok(service.getMyReferralsCredit(user, page, limit));
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.POKE + "/{pokedUserId}")
	public ResponseEntity<?> pokeUser(Authentication auth, @PathVariable Long pokedUserId)
	{
		Useraccount poker = useraccountDao.findByEmail(auth.getName());
		
		Useraccount pokedUser = useraccountDao.findById(pokedUserId).orElseThrow(() -> new NotFoundException("User not found!"));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.pokeUser(poker, pokedUser));
	}

	@PostMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.POKE + ApiRoute.CREDIT + "/{pokedUserId}")
	public ResponseEntity<?> pokeUserGetCredit(Authentication auth, @PathVariable Long pokedUserId)
	{
		Useraccount poker = useraccountDao.findByEmail(auth.getName());

		Useraccount pokedUser = useraccountDao.findById(pokedUserId).orElseThrow(() -> new NotFoundException("User not found!"));

		return ResponseEntity.status(HttpStatus.CREATED).body(service.pokeUserGetCredit(poker, pokedUser));
	}

	@PostMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.POKE + ApiRoute.CREDIT + ApiRoute.MAKE_REPAYMENT + "/{pokedUserId}")
	public ResponseEntity<?> pokeUserMakeRepaymentCredit(Authentication auth, @PathVariable Long pokedUserId)
	{
		Useraccount poker = useraccountDao.findByEmail(auth.getName());

		Useraccount pokedUser = useraccountDao.findById(pokedUserId).orElseThrow(() -> new NotFoundException("User not found!"));

		return ResponseEntity.status(HttpStatus.CREATED).body(service.pokeUserMakeRepaymentCredit(poker, pokedUser));
	}
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.REDEEM_BONUS)
	public ResponseEntity<?> redeemBonus(Authentication auth)
	{
		Useraccount poker = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.redeemBonus(poker));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.ACTIVITIES)
	public ResponseEntity<?> getMyReferralActivities(Authentication auth,
			@RequestParam(required = false, defaultValue =" 0") int page,
			@RequestParam(required = false, defaultValue = "10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getMyReferralBonusActivities(user, page, limit));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.REFERRALS + ApiRoute.BONUS + ApiRoute.IS_REDEEMABLE)
	public ResponseEntity<?> isRedeemable(Authentication auth)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.isRedeemable(user));
	}
}
