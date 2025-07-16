package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoricoRepository extends JpaRepository<StoricoTimbrature, Long> {

    // Metodo per cercare le timbrature che iniziano con la data specificata (dd/MM/yyyy)
    Page<StoricoTimbrature> findByTimestampStartingWith(String data, Pageable pageable);

}
