package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSheetMensileRepository extends JpaRepository<TimeSheetMensile, Long>
{

    Optional<TimeSheetMensile> findByUserIdAndAnnoAndMese(Long userId, int anno, int mese);
}
