package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.Auditorium;

@Getter
@Setter
public class CreateAuditoriumResponseDto {
    private Auditorium auditorium;
}
