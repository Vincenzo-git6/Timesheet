package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoricoRepository extends JpaRepository<StoricoTimbrature,Long> {
}
