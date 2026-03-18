package com.akshadip.bookmyseat.repositories;

import com.akshadip.bookmyseat.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
