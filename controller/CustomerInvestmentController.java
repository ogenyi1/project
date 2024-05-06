package ng.optisoft.rosabon.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.TopUpRequest;
import ng.optisoft.rosabon.dto.request.TransferRequest;
import ng.optisoft.rosabon.dto.request.WithdrawRequest;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.CustomerInvestmentMngr;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CustomerInvestmentController
{
	private final CustomerInvestmentMngr service;
	private final UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('TOPUP_INVESTMENT', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_INVESTMENT + ApiRoute.INITIATE_TOPUP)
	public ResponseEntity<?> initiateTopUp(Authentication auth, @RequestBody @NotNull @Valid TopUpRequest request)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		return ResponseEntity.ok(service.initiateTopUp(admin, request));
	}
	
	@PreAuthorize("hasAnyAuthority('TRANSFER_INVESTMENT', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_INVESTMENT + ApiRoute.TRANSFER)
	public ResponseEntity<?> transfer(Authentication auth, @RequestBody @NotNull @Valid TransferRequest request)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		return ResponseEntity.ok(service.transfer(admin, request));
	}
	
	@PreAuthorize("hasAnyAuthority('WITHDRAW_INVESTMENT', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_INVESTMENT + ApiRoute.WITHDRAW)
	public ResponseEntity<?> withdraw(Authentication auth, @RequestBody @NotNull @Valid WithdrawRequest request)
	{
		Useraccount admin = useraccountDao.findByEmail(auth.getName());
		return ResponseEntity.ok(service.withdraw(admin, request));
	}
}
