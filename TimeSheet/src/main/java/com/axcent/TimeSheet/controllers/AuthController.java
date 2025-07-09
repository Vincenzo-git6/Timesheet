package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetMensileRepository;
import com.axcent.TimeSheet.services.TimeSheetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileRepository timeSheetMensileRepository;


    @PostMapping("/timbratura")
    public ResponseEntity<?> timbra(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String nome = (String) request.getAttribute("nome");
        String cognome = (String) request.getAttribute("cognome");
        String sede = (String) request.getAttribute("sede");

        LocalDate oggi = LocalDate.now();
        LocalTime oraAttuale = LocalTime.now();
        int anno = oggi.getYear();
        int mese = oggi.getMonthValue();

        // Cerca o crea il TimeSheetMensile
        TimeSheetMensile mensile = timeSheetMensileRepository
                .findByUserIdAndAnnoAndMese(userId, anno, mese)
                .orElseGet(() -> {
                    TimeSheetMensile nuovo = new TimeSheetMensile();
                    nuovo.setUserId(userId);
                    nuovo.setNome(nome);
                    nuovo.setCognome(cognome);
                    nuovo.setSede(sede);
                    nuovo.setAnno(anno);
                    nuovo.setMese(mese);
                    return timeSheetMensileRepository.save(nuovo);
                });

        // Cerca o crea il TimeSheetGiornaliero per oggi
        TimeSheetGiornaliero giornaliero = mensile.getGiornalieri().stream()
                .filter(g -> g.getData().equals(oggi))
                .findFirst()
                .orElseGet(() -> {
                    TimeSheetGiornaliero nuovo = new TimeSheetGiornaliero();
                    nuovo.setData(oggi);
                    nuovo.setTimesheetMensile(mensile);
                    mensile.getGiornalieri().add(nuovo);
                    return nuovo;
                });

        // Timbra l’entrata se non c’è già
        if (giornaliero.getEntrataMattina() == null) {
            giornaliero.setEntrataMattina(oraAttuale);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Entrata mattina già timbrata per oggi");
        }

        timeSheetMensileRepository.save(mensile);

        return ResponseEntity.ok("Entrata timbrata alle " + oraAttuale);
    }



}
