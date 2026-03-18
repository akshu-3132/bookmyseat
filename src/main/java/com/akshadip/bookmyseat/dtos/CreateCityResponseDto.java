package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.City;

@Getter
@Setter
public class CreateCityResponseDto {
    private City city;
}
