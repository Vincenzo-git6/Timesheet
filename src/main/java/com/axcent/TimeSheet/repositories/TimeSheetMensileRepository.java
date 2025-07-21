package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeSheetMensileRepository extends JpaRepository<TimeSheetMensile, Long>
{



    List<TimeSheetMensile> findByUserIdAndAnnoOrderByAnnoAscMeseAsc(Long userId, Integer anno);

    @EntityGraph(attributePaths = "giornalieri")
    List<TimeSheetMensile> findAll(); // ora carica anche i giornalieri senza LazyInitException

    @EntityGraph(attributePaths = "giornalieri")
    Optional<TimeSheetMensile> findByUserIdAndAnnoAndMese(Long userId, int anno, int mese);

    @EntityGraph(attributePaths = "giornalieri")
    List<TimeSheetMensile> findByUserIdAndAnnoOrderByAnnoAscMeseAsc(Long userId, int anno);

    @EntityGraph(attributePaths = "giornalieri")
    List<TimeSheetMensile> findByUserIdOrderByAnnoAscMeseAsc(Long userId);

    @EntityGraph(attributePaths = "giornalieri")
    List<TimeSheetMensile> findByUserIdAndAnno(Long userId, int anno);


}
