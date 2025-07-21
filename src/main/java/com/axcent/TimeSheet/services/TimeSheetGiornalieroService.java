package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetGiornalieroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TimeSheetGiornalieroService
{
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroRepository timeSheetGiornalieroRepository;

    public TimeSheetGiornaliero createOrFindTimeSheetG(TimeSheetMensile mensile) {

        return mensile.getGiornalieri().stream()
                .filter(g -> g.getData().equals(LocalDate.now()))
                .findFirst()
                .orElseGet(() -> {
                    TimeSheetGiornaliero nuovo = new TimeSheetGiornaliero();
                    nuovo.setData(LocalDate.now());
                    nuovo.setTimesheetMensile(mensile);
                    // Salvo prima di aggiungere alla lista
                    TimeSheetGiornaliero saved = timeSheetGiornalieroRepository.save(nuovo);
                    mensile.getGiornalieri().add(saved);
                    return saved;
                });
    }

    public TimeSheetGiornaliero save(TimeSheetGiornaliero giornaliero) {
        return timeSheetGiornalieroRepository.save(giornaliero);
    }

    public TimeSheetGiornaliero findById(Long id){
        return timeSheetGiornalieroRepository.findById(id).orElse(null);
    }

    public TimeSheetGiornaliero getOrCreateByData(LocalDate data){
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroRepository.findByData(data);
        if(giornaliero==null){
            return new TimeSheetGiornaliero();
        }
        return giornaliero;

    }

}
