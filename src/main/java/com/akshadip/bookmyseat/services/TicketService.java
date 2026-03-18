package com.akshadip.bookmyseat.services;

import com.akshadip.bookmyseat.exceptions.ShowSeatNotAvailableException;
import com.akshadip.bookmyseat.models.ShowSeat;
import com.akshadip.bookmyseat.models.ShowSeatState;
import com.akshadip.bookmyseat.models.Ticket;
import com.akshadip.bookmyseat.models.TicketStatus;
import com.akshadip.bookmyseat.repositories.ShowRepository;
import com.akshadip.bookmyseat.repositories.ShowSeatRepository;
import com.akshadip.bookmyseat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TicketService {
    private ShowSeatRepository showSeatRepository;
    private UserRepository userRepository;
    private ShowRepository showRepository;

    @Autowired
    public TicketService(ShowSeatRepository showSeatRepository,
                         UserRepository userRepository,
                         ShowRepository showRepository){
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(Long showID, List<Long> showSeatIds, Long userId) throws ShowSeatNotAvailableException {
        // fetch given show seats
        List<ShowSeat> showSeats = showSeatRepository.findByIdIn(showSeatIds);

        // check if each of them are available
        for(ShowSeat showSeat : showSeats){
            if(showSeat.getState() != ShowSeatState.AVAILABLE){
                throw new ShowSeatNotAvailableException("Show Seat ID: " + showSeat.getId() + " not available");
            }
        }

        // update status to locked
        for(ShowSeat showSeat : showSeats){
            showSeat.setState(ShowSeatState.LOCKED);
            showSeatRepository.save(showSeat);
        }

        // create ticket object
        Ticket ticket = new Ticket();
        ticket.setTicketStatus(TicketStatus.PENDING);
        ticket.setBookedBy(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)));
        ticket.setShow(showRepository.findById(showID)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + showID)));
        ticket.setShowSeats(showSeats);
        ticket.setTimeOfBooking(new Date());

        // return ticket object
        return ticket;
    }
}
