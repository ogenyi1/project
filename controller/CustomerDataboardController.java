package ng.optisoft.rosabon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.PaginatedListDto;
import ng.optisoft.rosabon.dto.request.*;
import ng.optisoft.rosabon.dto.response.*;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.mapper.UseraccountMapper;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.model.Useraccount.Usage;
import ng.optisoft.rosabon.service.CustomerDataboardMngr;
import ng.optisoft.rosabon.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin
public class CustomerDataboardController
{
	@Value("${list-of-banks}")
	private String listOfBankUrl;
	
	@Value("${youverify-api-key}")
	private String youverifyToken;
	
	@Value("${base-url}")
	private String baseUrl;
	
	@Autowired private CustomerDataboardMngr service;
	@Autowired private UseraccountDao useraccountDao;
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.PERSONAL_INFO)
	public ResponseEntity<?> addPersonalInformation(Authentication auth, @RequestBody @Valid @NotNull PersonalInformationRequest request)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		CustomerDataboardDto dto = service.addPersonalInformation(user, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Profile added successfully", dto), HttpStatus.CREATED);
	}
	
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.ACCOUNT_OFFICER + "/{id}")
	public ResponseEntity<?> getAccountOfficerById(@PathVariable Long id)
	{
		Useraccount user = service.getAccountOfficerById(id);
		
		return ResponseEntity.ok(UseraccountMapper.mapToDto(user));
	}
	
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.ACCOUNT_OFFICER)
	public ResponseEntity<?> getAllAccountOfficers()
	{
		List<AdministratorDto> users = service.getAllAccountOfficers();
		
		return ResponseEntity.ok(users);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.PERSONAL_INFO + "/{email}")
	public ResponseEntity<?> editPersonalInfo(Authentication auth, @RequestBody @Valid @NotNull UpdatePersonalInformationRequest request, @PathVariable String email)
	{
		CustomerDataboardDto dto = service.editPersonalInfo(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Profile updated successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.EMPLOYMENT_DETAIL + "/{email}")
	public ResponseEntity<?> addEmploymentDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull EmploymentDetailInDto request)
	{
		EmploymentDetailDto dto = service.addEmploymentDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Employment Detail added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.EMPLOYMENT_DETAIL + "/{email}")
	public ResponseEntity<?> editEmploymentDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull UpdateEmploymentDetailRequest request)
	{
		EmploymentDetailDto dto = service.editEmploymentDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Employment Detail added successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.NOK_DETAIL + "/{email}")
	public ResponseEntity<?> addNokDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull NextOfKinDetailInDto request)
	{
		NextOfKinDetailDto dto = service.addNokDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Next Of Kin Detail added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.NOK_DETAIL + "/{email}")
	public ResponseEntity<?> editNokDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull NextOfKinDetailInDto request)
	{
		NextOfKinDetailDto dto = service.addNokDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Next Of Kin Detail added successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.MY_DOCUMENT + "/{email}")
	public ResponseEntity<?> addMyDocument(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull MyDocumentInDto request)
	{
		MyDocumentDto dto = service.addMyDocument(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "My Document added successfully", dto), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.MY_DOCUMENT + "/{email}")
	public ResponseEntity<?> editMyDocument(Authentication auth,
											@PathVariable String email,
											@RequestBody @Valid @NotNull UpdateMyDocumentRequest request
	) {
		MyDocumentDto dto = service.editMyDocument(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "My Document edited successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.BANK_ACCOUNT + "/{email}")
	public ResponseEntity<?> addBankAccountDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull BankAccountRequest request)
	{
		BankDto bankDto = null;
		
		if(getAllBanks().getStatusCode().equals(HttpStatus.OK))
		{
			BankListResponse bankListResponse = (BankListResponse) getAllBanks().getBody();
			
			bankDto = bankListResponse.getData().stream().filter(current -> request.getBankCode().equals(current.getCode())).findFirst().orElseThrow(() -> new NotFoundException("bank not found with given bank code"));
		}
		
		if(bankDto == null)
			throw new BadRequestException("bank cannot be null");
		
		BankAccountDto dto = service.createBankAccountDetails(email, request, bankDto);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Bank account added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.BANK_ACCOUNT + "/{email}")
	public ResponseEntity<?> editBankAccountDetails(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull UpdateBankAccountRequest request)
	{
		BankDto bankDto = null;
		
		if(getAllBanks().getStatusCode().equals(HttpStatus.OK))
		{
			BankListResponse bankListResponse = (BankListResponse) getAllBanks().getBody();
			
			bankDto = bankListResponse.getData()
					.stream()
					.filter(current -> request.getBankCode().equals(current.getCode()))
					.findFirst()
					.orElseThrow(()
							->
					new NotFoundException("bank not found with given bank code"));
		}
		
		if(bankDto == null)
			throw new BadRequestException("bank cannot be null");
		
		BankAccountDto dto = service.editBankAccountDetails(email, request, bankDto);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Bank account updated successfully", dto), HttpStatus.OK);
	}
	
//	@GetMapping(value = ApiRoute.BANK_ACCOUNT + ApiRoute.GET_ALL_BANKS)
	private ResponseEntity<?> getAllBanks()
	{
		try
		{
			String url = "https://" + baseUrl +  listOfBankUrl;
			
			//log.info("url : " + url);
			
			HttpHeaders headers = new HttpHeaders();
			
			//log.info(" --- token : " + youverifyToken);
			
			headers.set("token", youverifyToken);
			//headers.setContentType(MediaType.ALL);
			
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			RestTemplate restTemplate = new RestTemplate();
			
			ResponseEntity<BankListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, BankListResponse.class);
			
			//log.info("result : " + responseEntity.getBody());
			responseEntity.getBody().getData().stream().filter(current -> !current.getCode().isBlank()).sorted((a,b) -> a.getName().compareTo(b.getName()));
			
			return responseEntity;
		}
		catch(Exception e)
		{
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY)
	public ResponseEntity<?> addCompanyProfile(Authentication auth, @RequestBody @Valid @NotNull CompanyInformationRequest request)
	{
		Useraccount adminUser = useraccountDao.findByEmail(auth.getName());
		if(adminUser == null)
			throw new NotFoundException("User details not found");
		CompanyDto dto = service.addCompanyProfile(adminUser, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Company Profile added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + "/{userEmail}")
	public ResponseEntity<?> editCompanyProfile(Authentication auth, @RequestBody @Valid @NotNull UpdateCompanyInformationRequest request, @PathVariable String userEmail)
	{
		CompanyDto dto = service.editCompanyProfile(userEmail, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Company Profile updated successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + "/{email}")
	public ResponseEntity<?> addDirectorDetails(Authentication auth, @PathVariable String email, @RequestBody List<@Valid @NotNull DirectorDetailInDto> request)
	{
		List<DirectorDetailDto> dto = service.addDirectorDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Director details added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + ApiRoute.DIRECTOR_DETAILS + "/{email}")
	public ResponseEntity<?> editDirectorDetails(Authentication auth, @PathVariable String email, @RequestBody List<@Valid @NotNull UpdateDirectorDetailInDto> request)
	{
		List<DirectorDetailDto> dto = service.editDirectorDetails(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Director details updated successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PostMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT + "/{email}")
	public ResponseEntity<?> addCompanyDocument(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull CompanyDocumentInDto request)
	{
		CompanyDocumentDto dto = service.addCompanyDocument(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Company Document added successfully", dto), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + ApiRoute.COMPANY_DOCUMENT + "/{email}")
	public ResponseEntity<?> editCompanyDocument(Authentication auth, @PathVariable String email, @RequestBody @Valid @NotNull UpdateCompanyDocumentRequest request)
	{
		CompanyDocumentDto dto = service.editCompanyDocument(email, request);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Company Document updated successfully", dto), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.CUSTOMER_DETAILS + "/{id}")
	public ResponseEntity<?> getCustomerDetail(Authentication auth, @PathVariable Long id)
	{
		CustomerDataboardDto dto = service.getCustomerDetail(id);
		
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@CachePut("customers")
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.CUSTOMER_DETAILS)
	public ResponseEntity<?> getAllCustomerDetails(Authentication auth,
			@RequestParam(required = false, defaultValue = "TREASURY") Usage usage,
			@RequestParam(required = false) String search,
												   @RequestParam(defaultValue = "0") int page,
												   @RequestParam(defaultValue = "10") int limit)
	{
		PaginatedListDto<CustomerDataboardDto> dto = service.getAllCustomerDetails(usage, search, page, limit);
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY + "/{id}")
	public ResponseEntity<?> getCompanyDetail(Authentication auth, @PathVariable Long id)
	{
		CompanyDto dto = service.getCompanyDetail(id);
		
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CUSTOMER', 'SUPER_ADMINISTRATOR', 'COMPANY', 'INDIVIDUAL_USER')")
	@GetMapping(value = ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + ApiRoute.COMPANY)
	public ResponseEntity<?> getAllCompanyDetails(Authentication auth, @RequestParam(required = false, defaultValue = "TREASURY") Usage usage)
	{
		List<CompanyDto> dto = service.getAllCompanyDetails(usage);
		
		return ResponseEntity.ok(dto);
	}

	@Operation(
			summary = "Fetch account officers",
			description = "This endpoint is to be used to fetch account officers within the system primarily for the functionality " +
					"of editing a user's initially assigned account officer during his/her creation from admin portal. Should only be accessible to " +
					"'EDIT_CUSTOMER' and  'SUPER_ADMINISTRATOR' rights. The front-end developer will use the response to pass the values required in the editAccountOffice endpoint"
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@GetMapping(ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + "/fetch-account-officers")
	public ResponseEntity<?> fetchAccountOfficers() {
		return service.fetchAccountOfficers();
	}
	@Operation(
			summary = "Update user's account officer",
			description = "This endpoint is to be used to update a user's account officer within the system in conjunction with the fetchAccountOfficers()endpoint."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponseDto2.class)))
	@PreAuthorize("hasAnyAuthority('EDIT_CUSTOMER', 'SUPER_ADMINISTRATOR')")
	@PutMapping(ApiRoute.ADMIN + ApiRoute.CUSTOMER_DATABOARD + "/update-account-officer")
	public ResponseEntity<?> updateUserAccountOfficer(@Valid @RequestBody UpdateAccOfficerInDto dto) {
		return service.updateUserAccountOfficer(dto);
	}



}
