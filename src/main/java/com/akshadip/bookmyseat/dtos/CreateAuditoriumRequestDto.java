package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuditoriumRequestDto {
    private String name;
    private int capacity;
}
