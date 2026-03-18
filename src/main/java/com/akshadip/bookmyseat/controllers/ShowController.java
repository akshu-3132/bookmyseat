package com.akshadip.bookmyseat.controllers;

import com.akshadip.bookmyseat.dtos.CreateShowRequestDto;
import com.akshadip.bookmyseat.dtos.CreateShowResponseDto;
import com.akshadip.bookmyseat.models.Show;
import com.akshadip.bookmyseat.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    private ShowService showService;

    @Autowired
    public ShowController(ShowService showService){
        this.showService = showService;
    }

    @PostMapping
    public CreateShowResponseDto createShow(@RequestBody CreateShowRequestDto request){
        Show savedShow = showService.createShow(request.getMovieId(), request.getStartTime(), request.getEndTime(),
                request.getAuditoriumId(), request.getSeatPrices(), request.getLanguage());

        CreateShowResponseDto responseDto = new CreateShowResponseDto();
        responseDto.setShow(savedShow);

        return responseDto;
    }
}
