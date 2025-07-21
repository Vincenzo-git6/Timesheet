package com.axcent.TimeSheet.repositories;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.enums.Motivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.time.LocalDate;

public interface TimeSheetGiornalieroRepository extends JpaRepository<TimeSheetGiornaliero, Long>
{
    TimeSheetGiornaliero findByData(LocalDate data);

}
