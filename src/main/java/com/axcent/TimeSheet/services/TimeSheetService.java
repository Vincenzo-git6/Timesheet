package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.customHelper.LocalDateTimeForm;
import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.enums.Motivo;
import com.axcent.TimeSheet.repositories.TimeSheetGiornalieroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeSheetService
{
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final TimeSheetGiornalieroRepository timeSheetGiornalieroRepository;
    private final StoricoService storicoService;

//    public String timbraMattina(TimeSheetGiornaliero giornaliero,String username, String data)
//    {
//        LocalTime ora = LocalTime.now();
//
//        if (giornaliero.getEntrataMattina() == null) {
//            giornaliero.setEntrataMattina(ora);
//            storicoService.stampaLog(username,"Ingresso mattina",data);
//            return "Ingresso Mattina registrato";
//        } else if (giornaliero.getUscitaMattina() == null) {
//            storicoService.stampaLog(username,"Uscita mattina",data);
//            giornaliero.setUscitaMattina(ora);
//            return "Uscita Mattina registrata";
//        }
//
//        return "Timbratura mattina completata";
//    }

//    public String timbraPomeriggio(TimeSheetGiornaliero giornaliero,String username,String data) {
//        LocalTime ora = LocalTime.now();
//
//        if (giornaliero.getEntrataPomeriggio() == null) {
//            giornaliero.setEntrataPomeriggio(ora);
//            storicoService.stampaLog(username,"Ingresso Pomeriggio",data);
//            return "Ingresso Pomeriggio registrato";
//        } else if (giornaliero.getUscitaPomeriggio() == null) {
//            giornaliero.setUscitaPomeriggio(ora);
//            storicoService.stampaLog(username,"Uscita Pomeriggio",data);
//            return "Uscita Pomeriggio registrato";
//        }
//
//        return "Timbratura pomeridiana completata";
//    }

    public String timbraStraordinario(TimeSheetGiornaliero giornaliero,String username,String data) {
        LocalTime ora = LocalTime.now();

        if (giornaliero.getEntrataStraordinario() == null) {
            giornaliero.setEntrataStraordinario(ora);
            storicoService.stampaLog(username,"Ingresso Straordinario",data);
            return "Ingresso Straordinario registrato";

        } else if (giornaliero.getUscitaStraordinario() == null) {
            giornaliero.setUscitaStraordinario(ora);
            storicoService.stampaLog(username,"Uscita Straordinario",data);
            return "Uscita Straordinario registrato";
        }

        return "Timbratura straordinario completata";
    }

    public String timbraAssenza(TimeSheetGiornaliero giornaliero, Motivo motivo,String username,String data)
    {
        giornaliero.setMotivo(motivo);
        storicoService.stampaLog(username,"Assenza: "+motivo,data);
        return "Assenza registrata:" + motivo;
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


    public String timbraGenerale(TimeSheetGiornaliero giornaliero, String username, String data) {
        LocalTime ora = LocalTime.now();

        if (ora.isAfter(LocalTime.of(9, 0)) && ora.isBefore(LocalTime.of(14, 0)))
        {
            if (giornaliero.getEntrataMattina() == null) {
                giornaliero.setEntrataMattina(ora);
                storicoService.stampaLog(username, "Ingresso mattina", data);
                return "Ingresso Mattina registrato";
            } else if (giornaliero.getUscitaMattina() == null) {
                giornaliero.setUscitaMattina(ora);
                storicoService.stampaLog(username, "Uscita mattina", data);
                return "Uscita Mattina registrata";
            }
        } else if (ora.isAfter(LocalTime.of(14, 00)) && ora.isBefore(LocalTime.of(18, 5)))
        {
            if (giornaliero.getEntrataPomeriggio() == null) {
                giornaliero.setEntrataPomeriggio(ora);
                storicoService.stampaLog(username, "Ingresso pomeriggio", data);
                return "Ingresso Pomeriggio registrato";
            } else if (giornaliero.getUscitaPomeriggio() == null) {
                giornaliero.setUscitaPomeriggio(ora);
                storicoService.stampaLog(username, "Uscita pomeriggio", data);
                return "Uscita Pomeriggio registrata";
            }
        }

        return checkTimbratura(giornaliero);
    }

    public String checkTimbratura(TimeSheetGiornaliero giornaliero)
    {
        if (giornaliero.getEntrataMattina()!=null && giornaliero.getUscitaMattina()!=null && giornaliero.getEntrataPomeriggio()!=null && giornaliero.getUscitaPomeriggio()!=null)
            return "Timbratura già completata";

        return "Timbratura fuori orario consentito, ricordiamo che gli orari per le timbrature 'normali' sono: 09:00-13:00 e 14:00-18:00. " +
                "In caso di problemi contatta il reparto hr.";
    }

}

