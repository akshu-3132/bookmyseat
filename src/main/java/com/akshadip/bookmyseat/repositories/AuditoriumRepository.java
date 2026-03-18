package com.akshadip.bookmyseat.repositories;

import com.akshadip.bookmyseat.models.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
}
