package ng.optisoft.rosabon.controller;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.GeneralResponse;
import ng.optisoft.rosabon.dto.request.WalletTransactionHistoryInDto;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.WalletTransactionHistoryMngr;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class WalletTransactionHistoryController
{
	private final WalletTransactionHistoryMngr service;
	private final UseraccountDao useraccountDao;

	public ResponseEntity<?> recordTransaction(Authentication auth, @RequestBody @Valid @NotNull WalletTransactionHistoryInDto request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return new ResponseEntity<>(new GeneralResponse(HttpStatus.CREATED.value(), "Transaction recorded", service.recordTransaction(user, request, request.getSettlementId())), HttpStatus.CREATED);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.TRANSACTIONS + ApiRoute.HISTORY)
	public ResponseEntity<?> getTransactionHistory(Authentication auth,
			@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int limit)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getAllTransactionHistory(user, startDate, endDate, page, limit));
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.TRANSACTIONS + ApiRoute.HISTORY + "/{transactionRef}")
	public ResponseEntity<?> getTransactionByRef(Authentication auth, @PathVariable String transactionRef)
	{
//		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		return ResponseEntity.ok(service.getTransactionByRef(transactionRef));
	}
}
