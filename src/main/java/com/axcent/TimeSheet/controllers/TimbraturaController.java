package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.services.TimeSheetGiornalieroService;
import com.axcent.TimeSheet.services.TimeSheetMensileService;
import com.axcent.TimeSheet.services.TimeSheetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timbratura")
@RequiredArgsConstructor
public class TimbraturaController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final HttpServletRequest request;

    @PostMapping("/mattina")
    public ResponseEntity<?> timbraMattina() {

        Long userId = (Long) request.getAttribute("userId");

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraMattina(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("La timbratura mattutina è andata a buon fine");
    }


    @PostMapping("/pomeriggio")
    public ResponseEntity<?> timbraPomeriggio()
    {
        Long userId = (Long) request.getAttribute("userId");

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraPomeriggio(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("La timbratura pomeridiana è andata a buon fine");
    }

    @PostMapping("/straordinario")
    public ResponseEntity<?> timbraStraordinario()
    {
        Long userId = (Long) request.getAttribute("userId");

        TimeSheetMensile mensile = timeSheetMensileService.findOrCreateCurrentTimeSheetM(userId);
        TimeSheetGiornaliero giornaliero = timeSheetGiornalieroService.createOrFindTimeSheetG(mensile);

        timeSheetService.timbraStraordinario(giornaliero);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("La timbratura straordinaria è andata a buon fine");
    }

}
