package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import lombok.RequiredArgsConstructor;
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

    public void timbra(TimeSheetGiornaliero giornaliero) {
        LocalTime ora = LocalTime.now();

        if (ora.isBefore(LocalTime.of(13, 0))) {
            timbraMattina(giornaliero, ora);
        } else if (ora.isBefore(LocalTime.of(18, 0))) {
            timbraPomeriggio(giornaliero, ora);
        } else {
            timbraStraordinario(giornaliero, ora);
        }
    }

    private void timbraMattina(TimeSheetGiornaliero giornaliero, LocalTime ora) {
        if (giornaliero.getEntrataMattina() == null) {
            giornaliero.setEntrataMattina(ora);
        } else if (giornaliero.getUscitaMattina() == null) {
            giornaliero.setUscitaMattina(ora);
        } else {
            throw new IllegalStateException("Hai già completato la timbratura mattutina.");
        }
    }

    private void timbraPomeriggio(TimeSheetGiornaliero giornaliero, LocalTime ora) {
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

    private void timbraStraordinario(TimeSheetGiornaliero giornaliero, LocalTime ora) {
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

}
