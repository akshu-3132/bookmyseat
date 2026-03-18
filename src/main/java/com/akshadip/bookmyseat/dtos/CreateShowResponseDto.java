package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.Show;

@Getter
@Setter
public class CreateShowResponseDto {
    private Show show;
}
