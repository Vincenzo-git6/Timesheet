package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeSheetMensileRepository extends JpaRepository<TimeSheetMensile, Long>
{

    Optional<TimeSheetMensile> findByUserIdAndAnnoAndMese(Long userId, int anno, int mese);

    List<TimeSheetMensile> findByUserIdOrderByAnnoAscMeseAsc(Long userId);

    List<TimeSheetMensile> findByUserIdAndAnnoOrderByAnnoAscMeseAsc(Long userId, Integer anno);

    List<TimeSheetMensile> findByUserIdAndAnno(Long userId, int anno);
}
