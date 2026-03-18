package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.Language;
import com.akshadip.bookmyseat.models.SeatType;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class CreateShowRequestDto {
    private Long movieId;
    private Date startTime;
    private Date endTime;
    private Long auditoriumId;
    private Map<SeatType, Integer> seatPrices;
    private Language language;
}
