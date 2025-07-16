package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.entities.enums.Motivo;
import com.axcent.TimeSheet.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/timbratura")
@RequiredArgsConstructor
public class TimbraturaController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final HttpServletRequest request;
    private final LogService logService;
    private final CustomUtenteRepositoryImpl customUtenteRepository;

    @PostMapping("/mattina")
    public ResponseEntity<?> timbraMattina() {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        LocalDate oggi = LocalDate.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraMattina(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        logService.stampaLog(username, "Timbratura ingresso mattina", oggi);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Timbratura ingresso mattina effettuata con successo"
        ));
    }

    @PostMapping("/pomeriggio")
    public ResponseEntity<?> timbraPomeriggio() {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        LocalDate oggi = LocalDate.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraPomeriggio(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        logService.stampaLog(username, "Timbratura ingresso pomeriggio", oggi);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Timbratura ingresso pomeriggio effettuata con successo"
        ));
    }

    @PostMapping("/straordinario")
    public ResponseEntity<?> timbraStraordinario() {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        LocalDate oggi = LocalDate.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraStraordinario(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        logService.stampaLog(username, "Timbratura straordinario", oggi);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Timbratura straordinario effettuata con successo"
        ));
    }

    @PostMapping("/assenza")
    public ResponseEntity<?> dichiaraAssenza(@RequestParam Motivo motivo) {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        LocalDate oggi = LocalDate.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraAssenza(giornaliero, motivo);
        timeSheetGiornalieroService.save(giornaliero);

        logService.stampaLog(username, "Dichiarazione assenza: " + motivo, oggi);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Assenza registrata per motivo: " + motivo
        ));
    }
}
