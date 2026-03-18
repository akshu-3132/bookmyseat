package com.akshadip.bookmyseat.controllers;

import com.akshadip.bookmyseat.dtos.CreateCityRequestDto;
import com.akshadip.bookmyseat.dtos.CreateCityResponseDto;
import com.akshadip.bookmyseat.models.City;
import com.akshadip.bookmyseat.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private CityService cityService;

    @Autowired
    public CityController(CityService cityService){
        this.cityService = cityService;
    }

    @PostMapping
    public CreateCityResponseDto addCity(@RequestBody CreateCityRequestDto request){
        City savedCity = cityService.addCity(request.getName());

        CreateCityResponseDto response = new CreateCityResponseDto();
        response.setCity(savedCity);

        return response;
    }
}
