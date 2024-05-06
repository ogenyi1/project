package ng.optisoft.rosabon.controller;

import java.util.List;

import javax.validation.Valid;

import ng.optisoft.rosabon.model.Company;
import ng.optisoft.rosabon.model.IndividualUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.TicketInDto;
import ng.optisoft.rosabon.dto.request.UpdateTicketRequest;
import ng.optisoft.rosabon.dto.response.TicketDto;
import ng.optisoft.rosabon.mapper.TicketMapper;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity.Platform;
import ng.optisoft.rosabon.model.Ticket;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.service.TicketMngr;
import ng.optisoft.rosabon.util.DateTimeUtil;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class TicketController
{
	@Autowired
	private TicketMngr service;
	
	private UseraccountDao useraccountDao;
	private ModelMapper mapper;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK)
	public ResponseEntity<?> openTicket(Authentication auth, @RequestBody @Valid TicketInDto dto)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		Ticket ticket = service.openTicket(dto, user);
		
		TicketDto response = TicketMapper.mapToDto(ticket, mapper);
		
		return new ResponseEntity<>(new ResponseHelper(true, "Ticket created successfully", response), HttpStatus.CREATED);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.OPEN_TICKETS)
	public ResponseEntity<?> getMyOpenedTickets(Authentication auth,
			@RequestParam Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<TicketDto> response = service.getMyTickets(user, Ticket.TicketStatus.OPENED, platform, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.CLOSED_TICKETS)
	public ResponseEntity<?> getMyClosedickets(Authentication auth,
			@RequestParam Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<TicketDto> response = service.getMyTickets(user, Ticket.TicketStatus.CLOSED, platform, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.MY_TICKETS)
	public ResponseEntity<?> getMyTickets(Authentication auth,
			@RequestParam Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<TicketDto> response = service.getMyTickets(user, platform, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.EDIT_TICKET)
	public ResponseEntity<?> editTicket(Authentication auth, @RequestBody @Valid UpdateTicketRequest editableTicket, @RequestParam Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		Ticket ticket = service.editTicket(editableTicket, user, platform);
		
		return ResponseEntity.ok(TicketMapper.mapToDto(ticket, mapper));
	}
	
	@PreAuthorize("hasAnyAuthority('ARCHIVE_TICKET', 'CLOSE_TICKET', 'SUPER_ADMINISTRATOR')")
	@PatchMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.CLOSE_TICKET + "/{id}")
	public ResponseEntity<?> closeTicket(Authentication auth, @PathVariable Long id, @RequestParam Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		Ticket ticket = service.closeTicket(user, id, platform);
		
		TicketDto response = TicketMapper.mapToDto(ticket, mapper);
		
		return new ResponseEntity<>(new ResponseHelper(true, "This ticket has successfully been closed", response), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('REOPEN_TICKET', 'SUPER_ADMINISTRATOR')")
	@PatchMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.REOPEN_TICKET + "/{id}")
	public ResponseEntity<?> reOpenTicket(Authentication auth, @PathVariable Long id, @RequestParam Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		Ticket ticket = service.reOpenTicket(user, id, platform);

//		IndividualUser individualUser = getIndividualUser(ticket.getUseraccount());
//		Company company = getCompany(ticket.getUseraccount());
		
		TicketDto response = TicketMapper.mapToDto(ticket, mapper);
		
		return new ResponseEntity<>(new ResponseHelper(true, "This ticket has successfully been reopened", response), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_TICKET', 'SUPER_ADMINISTRATOR', 'VIEW_OPEN_TICKET')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.OPEN_TICKETS)
	public ResponseEntity<?> getAllOpenedTickets(Authentication auth,
			@RequestParam Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<TicketDto> response = service.getAllOpenedTickets(user, platform, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_TICKET', 'SUPER_ADMINISTRATOR', 'VIEW_ARCHIVED_TICKET')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + ApiRoute.ARCHIVED_TICKETS)
	public ResponseEntity<?> getAllArchivedTickets(Authentication auth,
			@RequestParam(defaultValue = "TREASURY") Platform platform,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		List<TicketDto> response = service.getAllArchivedTickets(user, platform, search, DateTimeUtil.getDate(from), DateTimeUtil.getDate(to));
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_TICKET', 'SUPER_ADMINISTRATOR')")
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.FEEDBACK + "/{id}")
	public ResponseEntity<?> getTicketById(Authentication auth, @PathVariable Long id, @RequestParam Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		TicketDto response = service.getTicketById(user, id, platform);
				
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + "/{id}")
	public ResponseEntity<?> getMyTicketById(Authentication auth, @PathVariable Long id, @RequestParam Platform platform)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		TicketDto response = service.getMyTicketById(user, id, platform);
				
		return ResponseEntity.ok(response);
	}

}
