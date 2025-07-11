package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.services.CustomUtenteRepositoryImpl;
import com.axcent.TimeSheet.services.TimeSheetGiornalieroService;
import com.axcent.TimeSheet.services.TimeSheetMensileService;
import com.axcent.TimeSheet.services.TimeSheetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimeSheetMensileController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;

    @PostMapping("/timbratura")
    public ResponseEntity<?> timbra(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbra(giornaliero);

        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("La timbratura è andata a buon fine");
    }
}
