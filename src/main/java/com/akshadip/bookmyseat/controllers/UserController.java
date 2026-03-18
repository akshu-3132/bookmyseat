package com.akshadip.bookmyseat.controllers;

import com.akshadip.bookmyseat.dtos.CreateUserRequestDto;
import com.akshadip.bookmyseat.dtos.CreateUserResponseDto;
import com.akshadip.bookmyseat.models.User;
import com.akshadip.bookmyseat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// if a class is annotated this way, spring will create a controller instance without us manually creating one
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto request){
        User savedUser = userService.createUser(request.getEmail());

        CreateUserResponseDto response = new CreateUserResponseDto();
        response.setUser(savedUser);
        return response;
    }
}
