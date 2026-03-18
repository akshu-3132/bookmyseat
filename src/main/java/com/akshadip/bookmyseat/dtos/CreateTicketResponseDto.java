package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.Ticket;

@Getter
@Setter
public class CreateTicketResponseDto {
    private Ticket ticket;
}
