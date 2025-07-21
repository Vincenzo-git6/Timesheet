package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.dtos.StatoTimbraturaDto;
import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.entities.customHelper.LocalDateTimeForm;
import com.axcent.TimeSheet.entities.enums.Motivo;
import com.axcent.TimeSheet.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/timbratura")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TimbraturaController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final HttpServletRequest request;
    private final StoricoService storicoService;
    private final CustomUtenteRepositoryImpl customUtenteRepository;

    @PostMapping("/timbra")
    public ResponseEntity<?> timbra() {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        String oggi = LocalDateTimeForm.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        String s = timeSheetService.timbraGenerale(giornaliero,username,oggi);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", s
        ));
    }


    @PostMapping("/straordinario")
    public ResponseEntity<?> timbraStraordinario() {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        String oggi = LocalDateTimeForm.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        String s = timeSheetService.timbraStraordinario(giornaliero,username,oggi);
        timeSheetGiornalieroService.save(giornaliero);


        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", s
        ));
    }

    @PostMapping("/assenza")
    public ResponseEntity<?> dichiaraAssenza(@RequestParam Motivo motivo) {
        Long userId = (Long) request.getAttribute("userId");
        String username = customUtenteRepository.getUtenteUsername(userId);
        String oggi = LocalDateTimeForm.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraAssenza(giornaliero, motivo,username,oggi);
        timeSheetGiornalieroService.save(giornaliero);


        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Assenza registrata per motivo: " + motivo
        ));
    }

    @GetMapping("/stato")
    public ResponseEntity<?> getStatoTimbrature(){
        Long userId =(Long) request.getAttribute("userId");
        LocalDate oggi = LocalDate.now();

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        StatoTimbraturaDto stato = new StatoTimbraturaDto();

        stato.setEntrataMattina(giornaliero.getEntrataMattina()!=null);
        stato.setUscitaMattina(giornaliero.getUscitaMattina()!=null);
        stato.setEntrataPomeriggio(giornaliero.getEntrataPomeriggio()!=null);
        stato.setUscitaPomeriggio(giornaliero.getUscitaPomeriggio()!=null);
        stato.setEntrataStraordinario(giornaliero.getEntrataStraordinario()!=null);
        stato.setUscitaStraordinario(giornaliero.getUscitaStraordinario()!=null);
        stato.setAssenzaSegnata(giornaliero.getMotivo()!=null);

        return ResponseEntity.ok(stato);

    }
}
