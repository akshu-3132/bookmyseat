package com.akshadip.bookmyseat.controllers;

import com.akshadip.bookmyseat.dtos.CreateTicketRequestDto;
import com.akshadip.bookmyseat.dtos.CreateTicketResponseDto;
import com.akshadip.bookmyseat.exceptions.ShowSeatNotAvailableException;
import com.akshadip.bookmyseat.models.Ticket;
import com.akshadip.bookmyseat.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping
    public CreateTicketResponseDto bookTicket(@RequestBody CreateTicketRequestDto request) throws ShowSeatNotAvailableException {
        Ticket bookedTicket = null;
        CreateTicketResponseDto responseDto = new CreateTicketResponseDto();
        try{
            bookedTicket = ticketService.bookTicket(request.getShowId(), request.getShowSeatIds(), request.getUserId());
            responseDto.setTicket(bookedTicket);
        } catch (ShowSeatNotAvailableException exception){
            System.out.println("Show seat unavailable");
        }

        return responseDto;
    }
}
