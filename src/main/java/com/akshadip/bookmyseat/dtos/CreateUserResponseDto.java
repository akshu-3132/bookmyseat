package com.akshadip.bookmyseat.dtos;

import lombok.Getter;
import lombok.Setter;
import com.akshadip.bookmyseat.models.User;

@Getter
@Setter
public class CreateUserResponseDto {
    private User user;
}
