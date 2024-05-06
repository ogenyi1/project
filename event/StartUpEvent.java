package ng.optisoft.rosabon.event;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import ng.optisoft.rosabon.dao.SpecialEarningActivityDao;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.util.GeneralUtil;
import ng.optisoft.rosabon.util.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Strings;

import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.AssignRoleRequest;
import ng.optisoft.rosabon.dto.request.ModuleInDto;
import ng.optisoft.rosabon.dto.request.ModuleItemInDto;
import ng.optisoft.rosabon.dto.request.RightInDto;
import ng.optisoft.rosabon.dto.request.RoleManagerInDto;
import ng.optisoft.rosabon.dto.response.RightDto;
import ng.optisoft.rosabon.enums.RoleConstant;
import ng.optisoft.rosabon.model.Right;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.model.Useraccount.Source;
import ng.optisoft.rosabon.service.RightMngr;
import ng.optisoft.rosabon.service.ModuleItemMngr;
import ng.optisoft.rosabon.service.ModuleMngr;
import ng.optisoft.rosabon.service.RoleManagerMngr;
import ng.optisoft.rosabon.service.UserManagementMngr;

@Component
@Slf4j
public class StartUpEvent
{
	@Autowired private UseraccountDao useraccountDao;
	@Autowired private RightMngr rightService;
	@Autowired private ModuleMngr moduleService;
	@Autowired private ModuleItemMngr moduleItemService;
	@Autowired private RoleManagerMngr roleManagerMngr;
	@Autowired private UserManagementMngr userManagementMngr;
	@Autowired private SpecialEarningActivityDao specialEarningActivityDao;
	
	@Value("${superadmin}")
	private String admin;
	
	@EventListener(ApplicationReadyEvent.class)
	public void onStartup()
	{
		System.out.println(" --- on startup --- ");
		generateTransactionIds();
		populateMyReferralCode();
		modifyReferralLinks();
		
			createModules();
			createModuleItems();
			createRights();

//		createRolesIfNotExists();
//		
//		createModuleItem();
		
		assignAllPermissionsToSuperUser();
	}

	private void createRights()
	{
		System.out.println(" --- creating rights --- ");
		List<RightInDto> treasuryRights = List.of(
				new RightInDto("CREATE_PRODUCT_CATEGORY", "PRODUCT_CATEGORY"),
				new RightInDto("EDIT_PRODUCT_CATEGORY", "PRODUCT_CATEGORY"),
				new RightInDto("VIEW_PRODUCT_CATEGORY", "PRODUCT_CATEGORY"),
				new RightInDto("CREATE_PRODUCT_PROPERTY", "PRODUCT_PROPERTY"),
				new RightInDto("EDIT_PRODUCT_PROPERTY", "PRODUCT_PROPERTY"),
				new RightInDto("VIEW_PRODUCT_PROPERTY", "PRODUCT_PROPERTY"),
				new RightInDto("EDIT_PRODUCT", "PRODUCTS"),
				new RightInDto("CREATE_PRODUCT", "PRODUCTS"),
				new RightInDto("VIEW_PRODUCT", "PRODUCTS"),
				new RightInDto("CREATE_WITHDRAWAL_REASON", "WITHDRAWAL_REASONS"),
				new RightInDto("EDIT_WITHDRAWAL_REASON", "WITHDRAWAL_REASONS"),
				new RightInDto("VIEW_WITHDRAWAL_REASON", "WITHDRAWAL_REASONS"),
				new RightInDto("DELETE_WITHDRAWAL_REASON", "WITHDRAWAL_REASONS"),
				new RightInDto("CREATE_INVESTMENT_RATE", "INVESTMENT_RATES"),
				new RightInDto("EDIT_INVESTMENT_RATE", "INVESTMENT_RATES"),
				new RightInDto("VIEW_INVESTMENT_RATE", "INVESTMENT_RATES"),
				new RightInDto("CREATE_WITHHOLDING_TAX_RATE", "WITHHOLDING_TAX_RATE"),
				new RightInDto("EDIT_WITHHOLDING_TAX_RATE", "WITHHOLDING_TAX_RATE"),
				new RightInDto("VIEW_WITHHOLDING_TAX_RATE", "WITHHOLDING_TAX_RATE"),
				new RightInDto("CREATE_PENAL_CHARGE", "PENAL_CHARGE"),
				new RightInDto("EDIT_PENAL_CHARGE", "PENAL_CHARGE"),
				new RightInDto("VIEW_PENAL_CHARGE", "PENAL_CHARGE"),
				new RightInDto("VIEW_WALLET_MANAGEMENT", "WALLET_MANAGEMENT"),
				new RightInDto("CREATE_TENOR", "TENOR_MANAGEMENT"),
				new RightInDto("EDIT_TENOR", "TENOR_MANAGEMENT"),
				new RightInDto("VIEW_TENOR", "TENOR_MANAGEMENT"),
				new RightInDto("CREATE_CURRENCY", "CURRENCY_MANAGEMENT"),
				new RightInDto("EDIT_CURRENCY", "CURRENCY_MANAGEMENT"),
				new RightInDto("VIEW_CURRENCY", "CURRENCY_MANAGEMENT"),
				new RightInDto("CREATE_EXCHANGE_RATE", "EXCHANGE_RATES"),
				new RightInDto("EDIT_EXCHANGE_RATE", "EXCHANGE_RATES"),
				new RightInDto("VIEW_EXCHANGE_RATE", "EXCHANGE_RATES"),
				new RightInDto("CREATE_PROCESS_FLOW", "MANAGE_PROCESS_STAGES"),
				new RightInDto("EDIT_PROCESS_FLOW", "MANAGE_PROCESS_STAGES"),
				new RightInDto("VIEW_PROCESS_FLOW", "MANAGE_PROCESS_STAGES"),
				new RightInDto("CREATE_STAGE", "MANAGE_PROCESS_STAGES"),
				new RightInDto("EDIT_STAGE", "MANAGE_PROCESS_STAGES"),
				new RightInDto("VIEW_STAGE", "MANAGE_PROCESS_STAGES"),
				new RightInDto("CREATE_CUSTOMER", "CUSTOMER_DATABOARD"),
				new RightInDto("EDIT_CUSTOMER", "CUSTOMER_DATABOARD"),
				new RightInDto("VIEW_CUSTOMER", "CUSTOMER_DATABOARD"),
				new RightInDto("EDIT_SOURCE", "SOURCE_MANAGEMENT"),
				new RightInDto("CREATE_SOURCE", "SOURCE_MANAGEMENT"),
				new RightInDto("VIEW_SOURCE", "SOURCE_MANAGEMENT"),
				new RightInDto("CREATE_GENDER", "GENDER_MANAGEMENT"),
				new RightInDto("EDIT_GENDER", "GENDER_MANAGEMENT"),
				new RightInDto("VIEW_GENDER", "GENDER_MANAGEMENT"),
				new RightInDto("CREATE_IDENTIFICATION_TYPE", "IDENTIFICATION_TYPE_SETUP"),
				new RightInDto("EDIT_IDENTIFICATION_TYPE", "IDENTIFICATION_TYPE_SETUP"),
				new RightInDto("VIEW_IDENTIFICATION_TYPE", "IDENTIFICATION_TYPE_SETUP"),
				new RightInDto("TOPUP_INVESTMENT", "CUSTOMER_INVESTMENT"),
				new RightInDto("CREATE_INVESTMENT", "CUSTOMER_INVESTMENT"),
				new RightInDto("TRANSFER_INVESTMENT", "CUSTOMER_INVESTMENT"),
				new RightInDto("WITHDRAW_INVESTMENT", "CUSTOMER_INVESTMENT"),
				new RightInDto("VIEW_REPORT", "REPORT"),
				new RightInDto("TREAT_REQUEST", "TREAT_REQUEST"),
				new RightInDto("VIEW_REQUEST", "VIEW_REQUEST"), //
				new RightInDto("CREATE_ROLE", "MANAGE_ROLES"),
				new RightInDto("EDIT_ROLE", "MANAGE_ROLES"),
				new RightInDto("VIEW_ROLE", "MANAGE_ROLES"),
				new RightInDto("CREATE_ADMIN_USER", "MANAGE_USERS"),
				new RightInDto("EDIT_ADMIN_USER", "MANAGE_USERS"),
				new RightInDto("VIEW_ADMIN_USER", "MANAGE_USERS"),
				new RightInDto("CREATE_BRANCH", "BRANCH_MANAGEMENT"),
				new RightInDto("EDIT_BRANCH", "BRANCH_MANAGEMENT"),
				new RightInDto("VIEW_BRANCH", "BRANCH_MANAGEMENT"),
				new RightInDto("CREATE_DEPARTMENT", "DEPARTMENT_MANAGEMENT"),
				new RightInDto("EDIT_DEPARTMENT", "DEPARTMENT_MANAGEMENT"),
				new RightInDto("VIEW_DEPARTMENT", "DEPARTMENT_MANAGEMENT"),
				new RightInDto("CREATE_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_TICKET", "VIEW_OPEN_TICKETS"),
				new RightInDto("CLOSE_TICKET", "ARCHIVE_TICKET"),
				new RightInDto("REOPEN_TICKET", "VIEW_OPEN_TICKETS"),
				new RightInDto("CREATE_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("EDIT_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("DELETE_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("VIEW_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("CREATE_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("CREATE_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("RESET_ADMIN_USER", "MANAGE_USERS")
//				new RightInDto("ALL_PERMISSIONS", "ALL_PERMISSIONS")
				);

		List<RightInDto> creditRights = List.of(
				new RightInDto("CREATE_PRODUCT_CATEGORY", "PRODUCT_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_PRODUCT_CATEGORY", "PRODUCT_CATEGORY_MANAGEMENT"),
				new RightInDto("DELETE_PRODUCT_CATEGORY", "PRODUCT_CATEGORY_MANAGEMENT"),
				new RightInDto("CREATE_TARGET_MARKET", "TARGET_MARKET_MANAGEMENT"),
				new RightInDto("EDIT_TARGET_MARKET", "TARGET_MARKET_MANAGEMENT"),
				new RightInDto("DELETE_TARGET_MARKET", "TARGET_MARKET_MANAGEMENT"),
				new RightInDto("CREATE_PRODUCT_TYPE", "PRODUCT_TYPE_MANAGEMENT"),
				new RightInDto("EDIT_PRODUCT_TYPE", "PRODUCT_TYPE_MANAGEMENT"),
				new RightInDto("DELETE_PRODUCT_TYPE", "PRODUCT_TYPE_MANAGEMENT"),
				new RightInDto("CREATE_REQUIREMENT", "REQUIREMENT_MANAGEMENT"),
				new RightInDto("EDIT_REQUIREMENT", "REQUIREMENT_MANAGEMENT"),
				new RightInDto("DELETE_REQUIREMENT", "REQUIREMENT_MANAGEMENT"),
				new RightInDto("CREATE_TOPUP", "TOPUP_MANAGEMENT"),
				new RightInDto("EDIT_TOPUP", "TOPUP_MANAGEMENT"),
				new RightInDto("CREATE_LIQUIDATION", "LIQUIDATION_MANAGEMENT"),
				new RightInDto("EDIT_LIQUIDATION", "LIQUIDATION_MANAGEMENT"),
				new RightInDto("CREATE_PENAL_CHARGE", "PENAL_CHARGE_MANAGEMENT"),
				new RightInDto("EDIT_PENAL_CHARGE", "PENAL_CHARGE_MANAGEMENT"),
				new RightInDto("CREATE_INTEREST_TYPE", "INTEREST_TYPE_MANAGEMENT"),
				new RightInDto("EDIT_INTEREST_TYPE", "INTEREST_TYPE_MANAGEMENT"),
				new RightInDto("DELETE_INTEREST_TYPE", "INTEREST_TYPE_MANAGEMENT"),
				new RightInDto("CREATE_REPAYMENT_TYPE", "REPAYMENT_TYPE_MANAGEMENT"),
				new RightInDto("EDIT_REPAYMENT_TYPE", "REPAYMENT_TYPE_MANAGEMENT"),
				new RightInDto("DELETE_REPAYMENT_TYPE", "REPAYMENT_TYPE_MANAGEMENT"),
				new RightInDto("CREATE_GROUP_COMPANY", "GROUP_COMPANY_MANAGEMENT"),
				new RightInDto("EDIT_GROUP_COMPANY", "GROUP_COMPANY_MANAGEMENT"),
				new RightInDto("DELETE_GROUP_COMPANY", "GROUP_COMPANY_MANAGEMENT"),
				new RightInDto("CREATE_PROFESSIONAL_OCCUPATION", "PROFESSIONAL_OCCUPATION_MANAGEMENT"),
				new RightInDto("EDIT_PROFESSIONAL_OCCUPATION", "PROFESSIONAL_OCCUPATION_MANAGEMENT"),
				new RightInDto("DELETE_PROFESSIONAL_OCCUPATION", "PROFESSIONAL_OCCUPATION_MANAGEMENT"),
				new RightInDto("CREATE_VEHICLE", "VEHICLE_MANAGEMENT"),
				new RightInDto("EDIT_VEHICLE", "VEHICLE_MANAGEMENT"),
				new RightInDto("DELETE_VEHICLE", "VEHICLE_MANAGEMENT"),
				new RightInDto("CREATE_INDUSTRY", "INDUSTRY_MANAGEMENT"),
				new RightInDto("EDIT_INDUSTRY", "INDUSTRY_MANAGEMENT"),
				new RightInDto("DELETE_INDUSTRY", "INDUSTRY_MANAGEMENT"),
				new RightInDto("CREATE_REGISTRATION", "REGISTRATION_MANAGEMENT"),
				new RightInDto("EDIT_REGISTRATION", "REGISTRATION_MANAGEMENT"),
				new RightInDto("DELETE_REGISTRATION", "REGISTRATION_MANAGEMENT"),
				new RightInDto("CREATE_PERFECTION", "PERFECTION_MANAGEMENT"),
				new RightInDto("EDIT_PERFECTION", "PERFECTION_MANAGEMENT"),
				new RightInDto("DELETE_PERFECTION", "PERFECTION_MANAGEMENT"),
				new RightInDto("CREATE_ADMIN_USER", "MANAGE_USERS"),
				new RightInDto("EDIT_ADMIN_USER", "MANAGE_USERS"),
//				new RightInDto("DELETE_USER", "MANAGE_USERS"),
				new RightInDto("CREATE_ROLE", "MANAGE_ROLES"),
				new RightInDto("EDIT_ROLE", "MANAGE_ROLES"),
//				new RightInDto("DELETE_ROLE", "MANAGE_ROLES"),
				new RightInDto("CREATE_BRANCH", "BRANCH_SETUP"),
				new RightInDto("EDIT_BRANCH", "BRANCH_SETUP"),
//				new RightInDto("DELETE_BRANCH", "BRANCH_SETUP"),
				new RightInDto("CREATE_DEPARTMENT", "DEPARTMENT_SETUP"),
				new RightInDto("EDIT_DEPARTMENT", "DEPARTMENT_SETUP"),
//				new RightInDto("DELETE_DEPARTMENT", "DEPARTMENT_SETUP"),
				new RightInDto("VIEW_OPEN_TICKET", "OPEN_TICKETS"),
				new RightInDto("VIEW_ARCHIVED_TICKET", "ARCHIVE_TICKET"),
				new RightInDto("CREATE_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
//				new RightInDto("DELETE_TICKET_CATEGORY", "TICKET_CATEGORY_MANAGEMENT"),
				new RightInDto("CREATE_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("EDIT_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("VIEW_MESSAGE", "MESSAGE_CENTRE"),
				new RightInDto("CREATE_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_FAQ", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("CREATE_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("EDIT_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_FAQ_CATEGORY", "FAQ_CATEGORY_MANAGEMENT"),
				new RightInDto("VIEW_TRANSACTION", "TRANSACTION_MANAGEMENT")
		);
		
		rightService.createRights(treasuryRights, GenericModuleBaseEntity.Platform.TREASURY);
		rightService.createRights(creditRights, GenericModuleBaseEntity.Platform.CREDIT);
	}
	
	public void createModuleItems() {
		System.out.println(" --- creating submodules --- ");
		List<ModuleItemInDto> treasuryModuleItems = List.of(
				new ModuleItemInDto("PRODUCT_CATEGORY", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PRODUCTS", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PRODUCT_PROPERTY", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("TENOR_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("WITHDRAWAL_REASONS", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("INVESTMENT_RATES", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("WITHHOLDING_TAX_RATE", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PENAL_CHARGE", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("CURRENCY_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("EXCHANGE_RATES", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("WALLET_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("MANAGE_PROCESS_STAGES", "PROCESS_MANAGEMENT"),
//				new ModuleItemInDto("EDIT_PROCESS_FLOW", "PROCESS_MANAGEMENT"),
				new ModuleItemInDto("CUSTOMER_DATABOARD", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("CUSTOMER_INVESTMENT", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("SOURCE_MANAGEMENT", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("GENDER_MANAGEMENT", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("IDENTIFICATION_TYPE_SETUP", "CUSTOMER_MANAGEMENT"),
//				new ModuleItemInDto("CREATE_IDENTIFICATION_TYPE", "CUSTOMER_MANAGEMENT"),
//				new ModuleItemInDto("EDIT_IDENTIFICATION_TYPE", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("MY_CUSTOMER", "MY_CUSTOMER"),
//				new ModuleItemInDto("CUSTOMER_DATABOARD", "CUSTOMER_MANAGEMENT"),
				new ModuleItemInDto("REPORT", "REPORT"),
				new ModuleItemInDto("TREAT_REQUEST", "REQUEST_CENTRE"), //
				new ModuleItemInDto("VIEW_REQUEST", "REQUEST_CENTRE"),
//				new ModuleItemInDto("MY_QUEUE", "REQUEST_CENTRE"),
				new ModuleItemInDto("MANAGE_USERS", "USER_MANAGEMENT"),
				new ModuleItemInDto("MANAGE_ROLES", "USER_MANAGEMENT"),
				new ModuleItemInDto("BRANCH_MANAGEMENT", "USER_MANAGEMENT"),
				new ModuleItemInDto("DEPARTMENT_MANAGEMENT", "USER_MANAGEMENT"),
//				new ModuleItemInDto("CREATE_BRANCH", "USER_MANAGEMENT"),
//				new ModuleItemInDto("EDIT_BRANCH", "USER_MANAGEMENT"),
//				new ModuleItemInDto("CREATE_DEPARTMENT", "USER_MANAGEMENT"),
//				new ModuleItemInDto("EDIT_DEPARTMENT", "USER_MANAGEMENT"),
				new ModuleItemInDto("VIEW_OPEN_TICKETS", "FEEDBACK"),
				new ModuleItemInDto("ARCHIVE_TICKET", "FEEDBACK"),
				new ModuleItemInDto("TICKET_CATEGORY_MANAGEMENT", "FEEDBACK"),
				new ModuleItemInDto("MESSAGE_CENTRE", "MESSAGE_CENTRE"),
//				new ModuleItemInDto("EDIT_MESSAGE", "MESSAGE_CENTRE"),
				new ModuleItemInDto("FAQ_CATEGORY_MANAGEMENT", "HELP_CENTRE")
//				new ModuleItemInDto("EDIT_FAQ", "HELP_CENTRE"),
//				new ModuleItemInDto("CREATE_FAQ_CATEGORY", "HELP_CENTRE"),
//				new ModuleItemInDto("EDIT_FAQ_CATEGORY", "HELP_CENTRE")
//				new ModuleItemInDto("ALL_PERMISSIONS", "ALL_PERMISSIONS")
				);
		List<ModuleItemInDto> creditModuleItems = List.of(
				new ModuleItemInDto("PRODUCT_CATEGORY_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("TARGET_MARKET_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PRODUCT_TYPE_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("REQUIREMENT_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("TOPUP_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("LIQUIDATION_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PENAL_CHARGE_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("INTEREST_TYPE_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("REPAYMENT_TYPE_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("GROUP_COMPANY_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PROFESSIONAL_OCCUPATION_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("VEHICLE_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("INDUSTRY_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("REGISTRATION_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("PERFECTION_MANAGEMENT", "PRODUCT_MANAGEMENT"),
				new ModuleItemInDto("MANAGE_USERS", "USER_MANAGEMENT"),
				new ModuleItemInDto("MANAGE_ROLES", "USER_MANAGEMENT"),
				new ModuleItemInDto("BRANCH_SETUP", "USER_MANAGEMENT"),
				new ModuleItemInDto("DEPARTMENT_SETUP", "USER_MANAGEMENT"),
				new ModuleItemInDto("OPEN_TICKETS", "FEEDBACK"),
				new ModuleItemInDto("ARCHIVE_TICKET", "FEEDBACK"),
				new ModuleItemInDto("TICKET_CATEGORY_MANAGEMENT", "FEEDBACK"),
				new ModuleItemInDto("MESSAGE_CENTRE", "MESSAGE_CENTRE"),
				new ModuleItemInDto("FAQ_CATEGORY_MANAGEMENT", "HELP_CENTRE"),
				new ModuleItemInDto("TRANSACTION_MANAGEMENT", "TRANSACTION_MANAGEMENT")
		);

		moduleItemService.createAllModuleItems(treasuryModuleItems, GenericModuleBaseEntity.Platform.TREASURY);
		moduleItemService.createAllModuleItems(creditModuleItems, GenericModuleBaseEntity.Platform.CREDIT);
	}

	private void createRolesIfNotExists(String roleName, GenericModuleBaseEntity.Platform platform)
	{
		List<RightDto> rights = rightService.getAllRights(platform);
		
		Set<String> rightNames = rights.stream()
				.map(right -> right.getRight())
				.collect(Collectors.toSet());
		
		RoleManagerInDto roleDto = new RoleManagerInDto();
		
		roleDto.setRoleName(roleName);
		roleDto.setDescription("For " + roleName);
		roleDto.setRights(rightNames);
		
		roleManagerMngr.createRole(roleDto, platform);
	}

	private void createModules()
	{
		System.out.println(" --- creating modules --- ");
		List<ModuleInDto> treasuryModules = List.of(
				new ModuleInDto("PRODUCT_MANAGEMENT", "For Product management module"),
				new ModuleInDto("PROCESS_MANAGEMENT", "For Process management module"),
				new ModuleInDto("CUSTOMER_MANAGEMENT", "For Customer management module"),
				new ModuleInDto("MY_CUSTOMER", "For My Customer module"),
				new ModuleInDto("REQUEST_CENTRE", "For the request centre module"),
				new ModuleInDto("USER_MANAGEMENT", "For the user management module"),
				new ModuleInDto("FEEDBACK", "For the feedback module"),
				new ModuleInDto("MESSAGE_CENTRE", "For message centre module"),
				new ModuleInDto("HELP_CENTRE", "For Help centre module"),
				new ModuleInDto("REPORT", "For report"),
				new ModuleInDto("TRANSACTION_MANAGEMENT", "For Transaction management module"),
				new ModuleInDto("ALL_PERMISSIONS", "For user with all permissions")
				);
		List<ModuleInDto> creditModules = List.of(
				new ModuleInDto("PRODUCT_MANAGEMENT", "For Product management module"),
				new ModuleInDto("USER_MANAGEMENT", "For the user management module"),
				new ModuleInDto("FEEDBACK", "For the feedback module"),
				new ModuleInDto("MESSAGE_CENTRE", "For message centre module"),
				new ModuleInDto("HELP_CENTRE", "For Help centre module"),
				new ModuleInDto("TRANSACTION_MANAGEMENT", "For Transaction management module")
				);
		
//		ModuleMngr moduleService = new ModuleMngrImpl();
		
		moduleService.createAllModules(treasuryModules, GenericModuleBaseEntity.Platform.TREASURY);
		moduleService.createAllModules(creditModules, GenericModuleBaseEntity.Platform.CREDIT);
	}

	private void assignAllPermissionsToSuperUser()
	{
		Useraccount user = useraccountDao.findByEmail(admin);
		if(Helper.isNotEmpty(user)){

			String roleName = "SUPER_ADMIN";
			
			createRolesIfNotExists(roleName, GenericModuleBaseEntity.Platform.TREASURY);

			AssignRoleRequest request = new AssignRoleRequest();

			request.setRoleName(roleName);
			request.setUserId(user.getId());

			userManagementMngr.assignRoleToUser(request);
		}

	}
	
	public void modifyReferralLinks() {
		useraccountDao.findAll()		.stream()
		.forEach(user -> {
			if(user.getRole().getName().equals(RoleConstant.ADMINISTRATOR) || user.getRole().getName().equals(RoleConstant.SUPER_ADMINISTRATOR)) {
				if(user.getReferralLink().endsWith("OTHER")) {
					String refLink = user.getReferralLink().replace("OTHER", "ROSABON_SALES");
					user.setReferralLink(refLink);
					useraccountDao.save(user);
				}
			}
			else {
				if(user.getReferralLink().endsWith("&source=OTHER")) {
					String refLink = user.getReferralLink().replace("&source=OTHER", "");
					user.setReferralLink(refLink);
					useraccountDao.save(user);
				}
			}
		});
	}
	
	public void populateMyReferralCode()
	{
//		log.info("recording user");
		
		useraccountDao.findAll().stream().forEach(user -> 
		{
			if(Strings.isNullOrEmpty(user.getMyReferralCode()))
			{
				String refCode = GeneralUtil.generateReferralCode(5);
				
				user.setMyReferralCode(refCode);
				
				Source source = user.getSource();
				
				String refUserLink = "https://rosabon-treasury.optisoft.com.ng/register-user?referralCode=" + refCode + "&source=" + source;
				
				user.setReferralLink(refUserLink);
				
				useraccountDao.save(user);
			}
		});
	}
	private void generateTransactionIds(){
		System.out.println("getting all transactions id that are null in special earning activity");
		specialEarningActivityDao.findAllByTransactionIdNull()
				.stream()
				.forEach(activity -> {
					System.out.println("Some values are actually null, generating...");
					activity.setTransactionId(GeneralUtil.generateRandomString(10));
					specialEarningActivityDao.save(activity);
				});
	}
}
