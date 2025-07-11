package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetGiornalieroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

@Service
@RequiredArgsConstructor
public class TimeSheetService
{
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final TimeSheetGiornalieroRepository timeSheetGiornalieroRepository;

//    public void timbra(TimeSheetGiornaliero giornaliero) {
//        LocalTime ora = LocalTime.now();
//
//        if (ora.isBefore(LocalTime.of(13, 0))) {
//            timbraMattina(giornaliero);
//        } else if (ora.isBefore(LocalTime.of(18, 0))) {
//            timbraPomeriggio(giornaliero, ora);
//        } else {
//            timbraStraordinario(giornaliero, ora);
//        }
//    }

    public void timbraMattina(TimeSheetGiornaliero giornaliero)
    {
        LocalTime ora = LocalTime.now();

        if (giornaliero.getEntrataMattina() == null) {
            giornaliero.setEntrataMattina(ora);
        } else if (giornaliero.getUscitaMattina() == null) {
            giornaliero.setUscitaMattina(ora);
        } else {
            throw new IllegalStateException("Hai già completato la timbratura mattutina.");
        }
    }

    public void timbraPomeriggio(TimeSheetGiornaliero giornaliero) {
        LocalTime ora = LocalTime.now();
        boolean mattinaCompletata = giornaliero.getUscitaMattina() != null ||
                (giornaliero.getEntrataMattina() == null && giornaliero.getUscitaMattina() == null);

        if (!mattinaCompletata) {
            throw new IllegalStateException("Devi completare o saltare la mattina prima di timbrare il pomeriggio.");
        }

        if (giornaliero.getEntrataPomeriggio() == null) {
            giornaliero.setEntrataPomeriggio(ora);
        } else if (giornaliero.getUscitaPomeriggio() == null) {
            giornaliero.setUscitaPomeriggio(ora);
        } else {
            throw new IllegalStateException("Hai già completato la timbratura pomeridiana.");
        }
    }

    public void timbraStraordinario(TimeSheetGiornaliero giornaliero) {
        LocalTime ora = LocalTime.now();
        boolean orarioNormaleCompletato =
                (giornaliero.getEntrataMattina() == null || giornaliero.getUscitaMattina() != null) &&
                        (giornaliero.getEntrataPomeriggio() == null || giornaliero.getUscitaPomeriggio() != null);

        if (!orarioNormaleCompletato) {
            throw new IllegalStateException("Completa o salta l'orario normale prima dello straordinario.");
        }

        if (giornaliero.getEntrataStraordinario() == null) {
            giornaliero.setEntrataStraordinario(ora);
        } else if (giornaliero.getUscitaStraordinario() == null) {
            giornaliero.setUscitaStraordinario(ora);
        } else {
            throw new IllegalStateException("Hai già completato lo straordinario.");
        }
    }

    public TimeSheetGiornaliero modificaEntrataMattina(Long giornalieroId, LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroRepository.findById(giornalieroId)
                .orElseThrow(()-> new RuntimeException("Nessun TimeSheet trovato con id: "+giornalieroId));
        giornaliero.setEntrataMattina(oraModificata);

        timeSheetGiornalieroService.save(giornaliero);

        return giornaliero;
    }

    public TimeSheetGiornaliero modificaUscitaMattina(Long giornalieroId, LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroRepository.findById(giornalieroId)
                .orElseThrow(()-> new RuntimeException("Nessun TimeSheet trovato con id: "+giornalieroId));
        giornaliero.setUscitaMattina(oraModificata);

        return giornaliero;
    }
    public TimeSheetGiornaliero modificaEntrataPomeriggio(Long giornalieroId, LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroRepository.findById(giornalieroId)
                .orElseThrow(()-> new RuntimeException("Nessun TimeSheet trovato con id: "+giornalieroId));
        giornaliero.setEntrataPomeriggio(oraModificata);

        return giornaliero;
    }

    public TimeSheetGiornaliero modificaUscitaPomeriggio(Long giornalieroId, LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroRepository.findById(giornalieroId)
                .orElseThrow(()-> new RuntimeException("Nessun TimeSheet trovato con id: "+giornalieroId));
        giornaliero.setUscitaPomeriggio(oraModificata);

        return giornaliero;
    }
}

