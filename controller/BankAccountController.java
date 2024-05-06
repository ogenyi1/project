package ng.optisoft.rosabon.controller;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.externalapis.paystack.service.PaystackHandler;
import ng.optisoft.rosabon.dao.BankDao;
import ng.optisoft.rosabon.dao.CompanyDao;
import ng.optisoft.rosabon.dao.IndividualUserDao;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.BvnVerification;
import ng.optisoft.rosabon.dto.request.BvnVerificationRequest;
import ng.optisoft.rosabon.dto.request.UpdateBankAccountRequest;
import ng.optisoft.rosabon.dto.request.paystack.AccountVerificationRequest;
import ng.optisoft.rosabon.dto.response.BankDto;
import ng.optisoft.rosabon.dto.response.BankListResponse;
import ng.optisoft.rosabon.dto.response.BvnVerificationResponse;
import ng.optisoft.rosabon.dto.response.paystack.PaystackAccountVerificationResponseDto;
import ng.optisoft.rosabon.enums.RoleConstant;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.mapper.BankAccountBusMapper;
import ng.optisoft.rosabon.mapper.BankAccountMapper;
import ng.optisoft.rosabon.mapper.BankMapper;
import ng.optisoft.rosabon.model.*;
import ng.optisoft.rosabon.service.BankAccountMngr;
import ng.optisoft.rosabon.service.UseraccountMngr;
import ng.optisoft.rosabon.service.ValidationService;
import ng.optisoft.rosabon.util.Helper;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountController
{
	@Value("${base-url}")
	private String baseUrl;

	@Value("${verify-bvn-url}")
	private String bvnUrl;

	@Autowired
	private UseraccountDao useraccountDao;

	@Autowired
	private IndividualUserDao individualUserDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private ValidationService valiationService;

	@Autowired
	private BankAccountMngr service;

	@Autowired
	private ModelMapper mapper;

	@Value("${list-of-banks}")
	private String listOfBankUrl;

	@Value("${verify-account-number}")
	private String verifyAccountUrl;

	@Value("${youverify-api-key}")
	private String youverifyToken;

	@Autowired
	private PaystackHandler paystackService;

	@Autowired
	private UseraccountMngr useraccountMngr;

	@Autowired
	private BankDao bankDao;

	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT)
	public ResponseEntity<?> updateBankDetails(Authentication auth, @RequestBody @Valid UpdateBankAccountRequest request) {
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		BankDto bankDto = null;
//		ResponseEntity<?> allBanks = null;
//
//		try {
//			allBanks = getAllBanks();
//		}catch (Exception e) {
//			log.info("----------------------------------------call to get YOU VERIFY supported banks most like failed----------------------------------------");
//			e.printStackTrace();
//		}
//
//		if (allBanks != null && allBanks.getStatusCode().equals(HttpStatus.OK)) {
//			BankListResponse bankListResponse = (BankListResponse) allBanks.getBody();
//			bankDto = bankListResponse.getData().stream().filter(current -> request.getBankCode()
//							.equals(current.getCode()))
//					.findFirst()
//					.orElseThrow(() -> new NotFoundException("bank not found with given bank code"));
//		}else {
			Bank bank = bankDao.findByCode(request.getBankCode());

			if (!Helper.isEmpty(bank)) bankDto = BankMapper.mapToDto(bank,mapper);
//		}



//		if(getAllBanks().getStatusCode().equals(HttpStatus.OK)) {
//			BankListResponse bankListResponse = (BankListResponse) getAllBanks().getBody();
//
//			bankDto = bankListResponse.getData().stream().filter(current -> request.getBankCode()
//					.equals(current.getCode())).
//					findFirst()
//					.orElseThrow(() -> new NotFoundException("NotFoundException"));
//		}

		if(bankDto == null)
			throw new NotFoundException("bank not found with given bank code");

		BankAccount bankAccount = service.updateBankAccountDetails(user, request, bankDto);

		return ResponseEntity.ok(BankAccountMapper.mapToDto(bankAccount, mapper));
	}

	@PutMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT_BUSINESS)
	public ResponseEntity<?> updateBankDetailsBusiness(Authentication auth, @RequestBody @Valid UpdateBankAccountRequest request) {
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		BankDto bankDto = null;

		Bank bank = bankDao.findByCode(request.getBankCode());

		if (!Helper.isEmpty(bank)) bankDto = BankMapper.mapToDto(bank,mapper);

//		if(getAllBanks().getStatusCode().equals(HttpStatus.OK)) {
//			BankListResponse bankListResponse = (BankListResponse) getAllBanks().getBody();
//
//			bankDto = bankListResponse.getData().stream().filter(current -> request.getBankCode().equals(current.getCode())).findFirst().orElseThrow(() -> new NotFoundException("bank not found with given bank code"));
//		}

		if(bankDto == null)
			throw new BadRequestException("bank not found with given bank code");

		BankAccountBus bankAccountBus = service.updateBankAccountDetailsBusiness(user, request, bankDto);

		return ResponseEntity.ok(BankAccountBusMapper.mapToDto(bankAccountBus, mapper));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT)
	public ResponseEntity<?> getBankDetailsByUser(Authentication auth) {
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		return ResponseEntity.ok(service.getBankByUser(user));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT_BUSINESS)
	public ResponseEntity<?> getBankDetailsBusByUser(Authentication auth) {
		Useraccount user = useraccountDao.findByEmail(auth.getName());

		return ResponseEntity.ok(service.getBankBusByUser(user));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT + "/{email}")
	public ResponseEntity<?> getBankDetailsByUser(Authentication auth, @PathVariable String email) {
		Useraccount user = useraccountDao.findByEmail(email);
		if(user == null)
			throw new NotFoundException("User details not found");

		return ResponseEntity.ok(service.getBankByUser(user));
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT_BUSINESS + "/{email}")
	public ResponseEntity<?> getBankDetailsBusByUser(Authentication auth, @PathVariable String email) {
		Useraccount user = useraccountDao.findByEmail(email);
		if(user == null)
			throw new NotFoundException("User details not found");

		return ResponseEntity.ok(service.getBankBusByUser(user));
	}

	@GetMapping(value = ApiRoute.BANK_ACCOUNT + ApiRoute.GET_ALL_BANKS)
	public ResponseEntity<?> getAllBanks() {
		try {
//			String url = "https://" + baseUrl +  listOfBankUrl;
//
//			//log.info("url : " + url);
//
//			HttpHeaders headers = new HttpHeaders();
//
//			//log.info(" --- token : " + youverifyToken);
//
//			headers.set("token", youverifyToken);
//			//headers.setContentType(MediaType.ALL);
//
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//
//			RestTemplate restTemplate = new RestTemplate();
//
//			ResponseEntity<BankListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, BankListResponse.class);
//
//			//log.info("result : " + responseEntity.getBody());
//			responseEntity.getBody().getData().stream().filter(current -> !current.getCode().isBlank()).sorted((a,b) -> a.getName().compareTo(b.getName()));

			List<BankDto> bankList = bankDao.findAll()
					.stream()
					.filter(bank -> (!bank.getName().equalsIgnoreCase("Paycom1")
							|| !bank.getName().equalsIgnoreCase("Paycom2")
							|| !bank.getName().equalsIgnoreCase("Paycom3")))
					.map(bank -> BankMapper.mapToDto(bank,mapper))
					.collect(Collectors.toList());

			BankListResponse bankListResponse = new BankListResponse(
					true,
					200,
					"suppoerted banks successfully fetched",
					bankList
			);

			return new ResponseEntity<>(
					bankListResponse,
					HttpStatus.OK
			);

		} catch(Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	@GetMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT + ApiRoute.SEND_OTP)
	public ResponseEntity<?> sendOtpToUser(Authentication auth) {
		Useraccount useraccount = useraccountDao.findByEmail(auth.getName());

		String otp = service.sendOtp(useraccount);

		return new ResponseEntity<>(new ResponseHelper(true, "An OTP have been sent to your registered email address, the OTP will expire after 10 mins", otp), HttpStatus.OK);
	}

	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT + ApiRoute.VERIFY)
	public ResponseEntity<?> verifyAccountNumber(Authentication auth, @RequestBody @Valid @NotNull AccountVerificationRequest request) {
		try {
			Useraccount useraccount;

			if (Strings.isNullOrEmpty(request.getUserId())) {
				useraccount = useraccountDao.findByEmail(auth.getName());
			}else{
				useraccount = useraccountDao.findById(
						Long.valueOf(
								request.getUserId())).
						orElseThrow(() -> new NotFoundException("user with given id was not found"));
			}

			if (useraccount == null)
				throw new NotFoundException("user to verify not found");

			String userFullName = null;

			if (!useraccount.getRole().getName().equals(RoleConstant.COMPANY)) {
				userFullName = useraccountMngr.getUserFullName(useraccount);
			}else {
				Company company = companyDao.findByUseraccount(useraccount);
				if (company.getContactFirstName() == null) throw new NotFoundException("contact person first name is not present");
				if (company.getContactLastName() == null) throw new NotFoundException("contact person last name is present");
				userFullName = company.getContactFirstName() + " " + company.getContactLastName();
			}

			if (userFullName == null) throw new NotFoundException("cannot find first and last name for this user");

			log.info("Name to be verified against is : {}",userFullName);

			String[] userFullNameArr = userFullName.split(" ");

			if (userFullNameArr.length < 2)
				throw new BadRequestException("User name on profile is incomplete. Update your first and last name");

			var accountVerificationResponse = paystackService.verifyAccountNumber(request);

			log.info("accountVerificationResponse:::::::::::::" + accountVerificationResponse);

			if(accountVerificationResponse == null || !accountVerificationResponse.getStatus())
				throw new BadRequestException("Unable to verify account number!");

			PaystackAccountVerificationResponseDto.DataBody accountVerificationResponseDto = accountVerificationResponse.getData();

			if (!accountVerificationResponseDto.getAccount_number().equals(request.getAccountNumber()) ) {
				throw new BadRequestException("Account number mismatch!");
			}

			String account_name = accountVerificationResponseDto.getAccount_name();

			String[] account_Name_Arr = account_name.split(" ");

			log.info("Name from call is : {}",account_name);
//			log.info("first Name from call : {}",account_Name_Arr[0]);
//			log.info("last Name from call : {}",account_Name_Arr[account_Name_Arr.length - 1]);

			int nameMatches = 0;

			for (String userName : userFullNameArr) {
				for (String accName : account_Name_Arr) {
					if (userName.equalsIgnoreCase(accName)) nameMatches++;
				}
			}

			log.info("names matched is : {}",nameMatches);

			if (nameMatches < 2) throw new BadRequestException("Account name mismatch!");

//			if (!account_Name_Arr[0].equalsIgnoreCase(firstName) &&
//					!account_Name_Arr[account_Name_Arr.length - 1]
//							.equalsIgnoreCase(lastName)) {
//				throw new BadRequestException("Account name mismatch!");
//			}

//			if (!accountVerificationResponseDto.getBank_id().equals(request.getBankCode())) {
//				throw new BadRequestException("Bank name mismatch!");
//			}

			Useraccount user = useraccountDao.findByEmail(auth.getName());

			if (!request.isVerifyBusinessBankAccount()) updateUserValidationStatus(user);
			else updateUserValidationStatusBusinessAccount(user);

			//log.info("result : " + responseEntity.getBody());

			return new ResponseEntity<>(
					new ResponseHelper(
							accountVerificationResponse.getStatus(),
							accountVerificationResponse.getMessage(),
							accountVerificationResponse.getData()
					)
					,
					HttpStatus.OK
			);
		} catch(Exception e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}

	private void updateUserValidationStatusBusinessAccount(Useraccount user) {
		if(user.getRole().getName().equals(RoleConstant.INDIVIDUAL_USER)) {
			IndividualUser individualUser = individualUserDao.findByUseraccount(user);

			individualUser.setBankAccountBusinessVerified(Boolean.TRUE);

			individualUserDao.save(individualUser);
		}
	}

//	@PostMapping(value = ApiRoute.AUTH + ApiRoute.INDIVIDUAL_USER + ApiRoute.BANK_ACCOUNT + ApiRoute.VERIFY)
//	public ResponseEntity<?> verifyAccountNumber(Authentication auth, @RequestBody @Valid @NotNull BankAccountVerificationRequest request)
//	{
//		try
//		{
//			String url = "https://" + baseUrl +  verifyAccountUrl;
//			
//			//log.info("url : " + url);
//			
//			HttpHeaders headers = new HttpHeaders();
//			
//			//log.info(" --- token : " + youverifyToken);
//			
//			headers.set("token", youverifyToken);
//			//headers.setContentType(MediaType.ALL);
//			
//			HttpEntity<BankAccountVerificationRequest> entity = new HttpEntity<>(request, headers);
//			
//			RestTemplate restTemplate = new RestTemplate();
//			
//			ResponseEntity<BankAccountVerificationResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, BankAccountVerificationResponse.class);
//			
//			if(responseEntity.getBody().getStatusCode() == HttpStatus.OK.value())
//			{
//				Useraccount user = useraccountDao.findByEmail(auth.getName());
//				
//				updateUserValidationStatus(user);
//			}
//			//log.info("result : " + responseEntity.getBody());
//						
//			return responseEntity;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			throw new BadRequestException(e.getMessage());
//		}
//	}

	private void updateUserValidationStatus(Useraccount user) {
		if(user.getRole().getName().equals(RoleConstant.INDIVIDUAL_USER)) {
			IndividualUser individualUser = individualUserDao.findByUseraccount(user);

			individualUser.setBankAccountVerified(Boolean.TRUE);

			individualUserDao.save(individualUser);
		}

		if(user.getRole().getName().equals(RoleConstant.COMPANY)) {
			Company company = companyDao.findByUseraccount(user);

			company.setIsBankAccountVerified(Boolean.TRUE);

			companyDao.save(company);
		}
	}

	@PostMapping(value = ApiRoute.AUTH + ApiRoute.VERIFY_BVN)
	public ResponseEntity<?> verifyBvn(Authentication auth, @RequestBody @Valid @NotNull BvnVerificationRequest request) {
		try {
			valiationService.validateObject(request);
			String url = "https://" + baseUrl +  bvnUrl;
			Useraccount user = useraccountDao.findByEmail(auth.getName());
			if(user == null)
				throw new NotFoundException("User details not found");
			BvnVerification data = new BvnVerification();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", youverifyToken);
			data.setId(request.getId());
			data.setIsSubjectConsent(request.getIsSubjectConsent());
			HttpEntity<BvnVerification> entity = new HttpEntity<>(data, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<BvnVerificationResponse> responseEntity = null;
			try {
				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, BvnVerificationResponse.class);
			}catch (Exception e) {
				e.printStackTrace();
				throw new NotFoundException(e.getMessage());
			}
			if(responseEntity.getBody() != null && !responseEntity.getBody().getSuccess())
				throw new BadRequestException("BVN Validation failed.");
			if(responseEntity.getBody() != null && responseEntity.getBody().getData().getStatus().equals("found")) {
				Boolean isNameMatched = false;
//				String mobile = responseEntity.getBody().getData().getMobile();
//				if(!Strings.isNullOrEmpty(request.getPhoneNumber())) {
//					if(request.getPhoneNumber().startsWith("+234")) {
//						String phoneNumber = request.getPhoneNumber().replace("+234", "0");
//						request.setPhoneNumber(phoneNumber);
//					}
//				}
//				if(!mobile.equals(request.getPhoneNumber()))
//					throw new BadRequestException("BVN Validation failed. Phone number not linked to BVN");
				if(!user.getRole().getName().equals(RoleConstant.COMPANY)) {
					LocalDate dob = responseEntity.getBody().getData().getDateOfBirth();
					if(!dob.equals(request.getDateOfBirth()))
						throw new BadRequestException("BVN Validation failed. Date of birth not matched");
				}
				if(Strings.isNullOrEmpty(request.getFirstName()) || Strings.isNullOrEmpty(request.getLastName()))
					throw new BadRequestException("First name and Last name are required");
				String firstName = responseEntity.getBody().getData().getFirstName();
				String lastName = responseEntity.getBody().getData().getLastName();
				String fullName = firstName + " " + lastName;
				if(firstName.trim().equalsIgnoreCase(request.getFirstName().trim()) && lastName.trim().equalsIgnoreCase(request.getLastName().trim()))
					isNameMatched = true;
				else if(fullName.toUpperCase().contains(request.getFirstName().trim().toUpperCase()) ||
						fullName.toUpperCase().contains(request.getLastName().trim().toUpperCase())
				)
					isNameMatched = false;
				else
					throw new BadRequestException("None of the inputted names matches the ones found on the bvn");
				responseEntity.getBody().setIsNameMatched(isNameMatched);
			} else
				throw new BadRequestException("BVN Validation failed. Please provide a valid BVN");

			return responseEntity;
		} catch(Exception e) {
			e.printStackTrace();
			throw new BadRequestException(e.getMessage());
		}
	}

	@PostMapping(value = ApiRoute.AUTH + ApiRoute.VERIFY_BVN + ApiRoute.CREDIT)
	public ResponseEntity<?> verifyBvnCredit(Authentication auth, @RequestBody @Valid @NotNull BvnVerificationRequest request) {
		try {
			valiationService.validateObject(request);

			String url = "https://" + baseUrl +  bvnUrl;

			//log.info("url : " + url);

			BvnVerification data = new BvnVerification();

			HttpHeaders headers = new HttpHeaders();

			//log.info(" --- token : " + youverifyToken);

			headers.set("token", youverifyToken);
			//headers.setContentType(MediaType.ALL);

			data.setId(request.getId());
			data.setIsSubjectConsent(request.getIsSubjectConsent());

			HttpEntity<BvnVerification> entity = new HttpEntity<>(data, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<BvnVerificationResponse> responseEntity;

			try {
				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, BvnVerificationResponse.class);
			}catch (Exception e) {
				e.printStackTrace();
				throw new NotFoundException(e.getMessage());
			}
			log.info("{}",responseEntity.getBody());

			if (responseEntity.getBody() != null && !responseEntity.getBody().getSuccess()) throw new BadRequestException("Bvn was not found");

			if(Objects.requireNonNull(responseEntity.getBody()).getData().getStatus().equals("found")) {
				boolean isNameMatched = false;

				String firstName = responseEntity.getBody().getData().getFirstName();

				String lastName = responseEntity.getBody().getData().getLastName();

				if(firstName.trim().equalsIgnoreCase(request.getFirstName().trim()) && lastName.trim().equalsIgnoreCase(request.getLastName().trim()))
					isNameMatched = true;

				responseEntity.getBody().setIsNameMatched(isNameMatched);

			}else {
				throw new BadRequestException("Bvn was not found");
			}
			return responseEntity;
		} catch(Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
}