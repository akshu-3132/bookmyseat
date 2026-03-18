package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.akshadip.bookmyseat.models.Seat;

@Getter
@Setter
public class AddSeatsResponseDto {
    private String message;
    private int totalSeatsCreated;
    private List<Seat> seats;
}
