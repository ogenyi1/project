package ng.optisoft.rosabon.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.request.TicketReplyInDto;
import ng.optisoft.rosabon.dto.response.TicketReplyDto;
import ng.optisoft.rosabon.mapper.TicketReplyMapper;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity.Platform;
import ng.optisoft.rosabon.model.TicketReply;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.model.Useraccount.Usage;
import ng.optisoft.rosabon.service.TicketReplyMngr;
import ng.optisoft.rosabon.util.ResponseHelper;

@RestController
@CrossOrigin
@AllArgsConstructor
public class TicketReplyController
{
	@Autowired
	private TicketReplyMngr service;
	
	private UseraccountDao useraccountDao;
	private ModelMapper mapper;
	
	@PostMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.REPLY_TICKET)
	public ResponseEntity<?> replyTicket(Authentication auth, @RequestBody @Valid TicketReplyInDto dto)
	{
		Useraccount user = useraccountDao.findByEmail(auth.getName());
		
		TicketReply ticketReply = service.replyTicket(user, dto);
		
		TicketReplyDto response = TicketReplyMapper.mapToDto(ticketReply, mapper);
		
		return new ResponseEntity<>(new ResponseHelper(true, "ticket reply created successfully", response), HttpStatus.CREATED);
	}
	
	@GetMapping(value = ApiRoute.AUTH + ApiRoute.FEEDBACK + ApiRoute.TICKET_REPLY + "/{ticketId}")
	public ResponseEntity<?> getAllReplies(Authentication auth, @PathVariable Long ticketId)
	{
		List<TicketReplyDto> replies = service.getAllReplies(ticketId);
		
//		TicketReplyDto response = TicketReplyMapper.mapToDto(ticketReply, mapper);
		
		return ResponseEntity.ok(replies);
	}
}
