package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetMensileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TimeSheetMensileService
{
    private final TimeSheetMensileRepository timeSheetMensileRepository;
    private final CustomUtenteRepositoryImpl custom;
    //nei servizi vanno le repository

    public TimeSheetMensile saveTimeSheetM(TimeSheetMensile timeSheetMensile)
    {
        try{
            return timeSheetMensileRepository.save(timeSheetMensile);
        }
        catch (Exception ex){ throw new RuntimeException("Il timesheet non è stato salvato"); }
    }

    public TimeSheetMensile findOrCreateTimeSheetM(Long userId, int anno, int mese)
    {
        try {
            Map<String, Object> m = custom.getUtenteInfo(userId);
            return timeSheetMensileRepository
                    .findByUserIdAndAnnoAndMese(userId, anno, mese)
                    .orElseGet(() -> {
                        TimeSheetMensile nuovo = new TimeSheetMensile();
                        nuovo.setUserId(userId);
                        nuovo.setAnno(anno);
                        nuovo.setMese(mese);
                        nuovo.setNome(m.getOrDefault("nome",null).toString());
                        nuovo.setCognome(m.getOrDefault("cognome",null).toString());
                        nuovo.setSede(m.getOrDefault("sede",null).toString());

                        return saveTimeSheetM(nuovo);
                    });
        } catch (Exception ex) {
            throw new RuntimeException("Errore nella ricerca o creazione del timesheet", ex);
        }
    }
    public TimeSheetMensile findOrCreateCurrentTimeSheetM(Long userId) {
        try {
            LocalDate oggi = LocalDate.now();
            LocalTime oraAttuale = LocalTime.now();

            int anno = oggi.getYear();
            int mese = oggi.getMonthValue();

            Map<String, Object> m = custom.getUtenteInfo(userId);
            return timeSheetMensileRepository
                    .findByUserIdAndAnnoAndMese(userId, anno, mese)
                    .orElseGet(() -> {
                        TimeSheetMensile nuovo = new TimeSheetMensile();
                        nuovo.setUserId(userId);
                        nuovo.setAnno(anno);
                        nuovo.setMese(mese);
                        nuovo.setNome(m.getOrDefault("nome",null).toString());
                        nuovo.setCognome(m.getOrDefault("cognome",null).toString());
                        nuovo.setSede(m.getOrDefault("sede",null).toString());

                        return saveTimeSheetM(nuovo);
                    });
        } catch (Exception ex) {
            throw new RuntimeException("Errore nella ricerca o creazione del timesheet", ex);
        }
    }


}
