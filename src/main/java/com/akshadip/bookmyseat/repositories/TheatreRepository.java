package com.akshadip.bookmyseat.repositories;

import com.akshadip.bookmyseat.models.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Theatre save(Theatre theatre);
}
