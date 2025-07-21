package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.dtos.TimeSheetMensileDto;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetMensileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSheetMensileService
{
    private final TimeSheetMensileRepository timeSheetMensileRepository;
    private final CustomUtenteRepositoryImpl custom;
    private final ExcelService excelService;

    public TimeSheetMensile getById(Long id)
    {
        return timeSheetMensileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

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

    public List<TimeSheetMensileDto> getByUserIdAndAnnoDto(Long userId, Integer anno) {
        List<TimeSheetMensile> list = (anno != null)
                ? timeSheetMensileRepository.findByUserIdAndAnnoOrderByAnnoAscMeseAsc(userId, anno)
                : timeSheetMensileRepository.findByUserIdOrderByAnnoAscMeseAsc(userId);

        return list.stream()
                .map(m -> new TimeSheetMensileDto(m, m.getGiornalieri()))
                .collect(Collectors.toList());
    }

    public TimeSheetMensileDto getDtoByUserAnnoEMese(Long userId, int anno, int mese) {
        TimeSheetMensile m = timeSheetMensileRepository.findByUserIdAndAnnoAndMese(userId, anno, mese).orElse(null);
        return (m != null) ? new TimeSheetMensileDto(m, m.getGiornalieri()) : null;
    }

    public byte[] generateExcelByUserAnnoEMese(Long userId, int anno, int mese) throws IOException {
        TimeSheetMensile m = timeSheetMensileRepository.findByUserIdAndAnnoAndMese(userId, anno, mese).orElse(null);
        if (m == null) throw new NoSuchElementException();
        return excelService.generateTimesheetExcel(m);
    }

    public List<TimeSheetMensile> getMensiliByUserIdAndAnno(Long userId, int anno) {
        return timeSheetMensileRepository.findByUserIdAndAnno(userId, anno);
    }
    public TimeSheetMensile getMensiliByUserIdAndAnnoAndMese(Long userId, int anno,int mese) {
        return timeSheetMensileRepository.findByUserIdAndAnnoAndMese(userId,anno,mese).orElse(null);
    }

    public List<TimeSheetMensile> findAllTimesheet(){
        return timeSheetMensileRepository.findAll();
    }


    public TimeSheetMensile findTimesheetById(Long id) {
        return timeSheetMensileRepository.findById(id).orElse(null);
    }
}
