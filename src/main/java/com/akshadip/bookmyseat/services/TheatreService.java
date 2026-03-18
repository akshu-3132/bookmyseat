package com.akshadip.bookmyseat.services;

import com.akshadip.bookmyseat.exceptions.CityNotFoundException;
import com.akshadip.bookmyseat.models.*;
import com.akshadip.bookmyseat.repositories.AuditoriumRepository;
import com.akshadip.bookmyseat.repositories.CityRepository;
import com.akshadip.bookmyseat.repositories.SeatRepository;
import com.akshadip.bookmyseat.repositories.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TheatreService {
    private TheatreRepository theatreRepository;
    private CityRepository cityRepository;
    private AuditoriumRepository auditoriumRepository;
    private SeatRepository seatRepository;

    @Autowired
    public TheatreService(TheatreRepository theatreRepository,
                          CityRepository cityRepository,
                          AuditoriumRepository auditoriumRepository,
                          SeatRepository seatRepository){
        this.theatreRepository = theatreRepository;
        this.cityRepository = cityRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.seatRepository = seatRepository;
    }

    public Theatre createTheatre(String name, String address, Long id) throws CityNotFoundException {
        // check if city with ID exists
        Optional<City> cityOptional = cityRepository.findById(id);
        if(!cityOptional.isPresent()){
            throw new CityNotFoundException("No city with given ID");
        }

        // create theatre object
        Theatre theatre = new Theatre();
        theatre.setName(name);
        theatre.setAddress(address);

        // save it to database
        Theatre savedTheatre = theatreRepository.save(theatre);

        // fetch the city for the ID
        City dbCity = cityOptional.get();

        // add the theatre to the city
        dbCity.getTheatres().add(savedTheatre);

        // update the city in the database
        // according to JPA, if we try to save what is already existing, it will update
        this.cityRepository.save(dbCity);

        return savedTheatre;
    }

    public Theatre addAuditorium(Long theatreId, String name, int capacity){
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with ID: " + theatreId));

        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setCapacity(capacity);
        auditorium.setTheatre(theatre);

        Auditorium savedAuditorium = auditoriumRepository.save(auditorium);

        theatre.getAuditoriums().add(savedAuditorium);

        return theatreRepository.save(theatre);
    }

    public List<Seat> addSeatsToAuditorium(Long auditoriumId, Map<SeatType, Integer> seatCount){
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
                .orElseThrow(() -> new RuntimeException("Auditorium not found with ID: " + auditoriumId));

        List<Seat> seats = new ArrayList<>();

        for(Map.Entry<SeatType, Integer> entry: seatCount.entrySet()){
            for(int i = 0; i < entry.getValue(); i++){
                Seat seat = new Seat();
                seat.setSeatType(entry.getKey());
                seat.setSeatNumber(entry.getKey().toString() + (i + 1));
                seats.add(seat);
            }
        }

        List<Seat> savedSeats = seatRepository.saveAll(seats);

        auditorium.setSeats(savedSeats);

        auditoriumRepository.save(auditorium);
        
        return savedSeats;
    }
}
