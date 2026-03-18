package com.akshadip.bookmyseat.controllers;

import com.akshadip.bookmyseat.dtos.*;
import com.akshadip.bookmyseat.exceptions.CityNotFoundException;
import com.akshadip.bookmyseat.models.SeatType;
import com.akshadip.bookmyseat.models.Theatre;
import com.akshadip.bookmyseat.models.Auditorium;
import com.akshadip.bookmyseat.models.Seat;
import com.akshadip.bookmyseat.services.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {
    private TheatreService theatreService;

    @Autowired
    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    /**
     * Create a new theatre in a specific city
     * POST /api/theatres/{cityId}
     * 
     * @param cityId City ID where theatre will be created
     * @param request Theatre details (name, address)
     * @return Created theatre response
     */
    @PostMapping("/{cityId}")
    public ResponseEntity<CreateTheatreResponseDto> createTheatre(
            @PathVariable Long cityId,
            @RequestBody CreateTheatreRequestDto request) {
        try {
            Theatre savedTheatre = theatreService.createTheatre(
                    request.getName(),
                    request.getAddress(),
                    cityId
            );
            CreateTheatreResponseDto response = new CreateTheatreResponseDto();
            response.setTheatre(savedTheatre);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Add a new auditorium to a theatre
     * POST /api/theatres/{theatreId}/auditoriums
     * 
     * @param theatreId Theatre ID to add auditorium to
     * @param request Auditorium details (name, capacity)
     * @return Created auditorium response
     */
    @PostMapping("/{theatreId}/auditoriums")
    public ResponseEntity<CreateAuditoriumResponseDto> addAuditorium(
            @PathVariable Long theatreId,
            @RequestBody CreateAuditoriumRequestDto request) {
        try {
            Theatre theatre = theatreService.addAuditorium(
                    theatreId,
                    request.getName(),
                    request.getCapacity()
            );
            
            // Get the newly added auditorium (last one in the list)
            Auditorium newAuditorium = theatre.getAuditoriums()
                    .get(theatre.getAuditoriums().size() - 1);
            
            CreateAuditoriumResponseDto response = new CreateAuditoriumResponseDto();
            response.setAuditorium(newAuditorium);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Add seats to an auditorium
     * POST /api/theatres/{auditoriumId}/seats
     * 
     * @param auditoriumId Auditorium ID to add seats to
     * @param seatCount Map of seat types to count (e.g., {"REGULAR": 50, "RECLINE": 30})
     * @return Response with created seats information
     */
    @PostMapping("/{auditoriumId}/seats")
    public ResponseEntity<AddSeatsResponseDto> addSeatsToAuditorium(
            @PathVariable Long auditoriumId,
            @RequestBody Map<SeatType, Integer> seatCount) {
        try {
            List<Seat> createdSeats = theatreService.addSeatsToAuditorium(auditoriumId, seatCount);
            
            AddSeatsResponseDto response = new AddSeatsResponseDto();
            response.setMessage("Seats added successfully");
            response.setTotalSeatsCreated(createdSeats.size());
            response.setSeats(createdSeats);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
