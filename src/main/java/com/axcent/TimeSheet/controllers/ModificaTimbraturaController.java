package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.services.StoricoService;
import com.axcent.TimeSheet.services.TimeSheetGiornalieroService;
import com.axcent.TimeSheet.services.TimeSheetMensileService;
import com.axcent.TimeSheet.services.TimeSheetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/modifica")
@RequiredArgsConstructor
public class ModificaTimbraturaController
{
    private final TimeSheetService timeSheetService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;
    private final HttpServletRequest request;
    private final StoricoService storicoService;



    @PostMapping("/entrataMattina")
    public ResponseEntity<?> modificaEntrataM(@RequestParam Long timeSheetGiornalieroId,@RequestParam LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetService.modificaEntrataMattina(timeSheetGiornalieroId,oraModificata);

        return ResponseEntity.ok(oraModificata + " Entrata Mattina : modificata con successo" );
    }

    @PostMapping("/uscitaMattina")
    public ResponseEntity<?> modificaUscitaM(@RequestParam Long timeSheetGiornalieroId,@RequestParam LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetService.modificaUscitaMattina(timeSheetGiornalieroId,oraModificata);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("Uscita Mattina : modificata con successo");
    }

    @PostMapping("/entrataPomeriggio")
    public ResponseEntity<?> modificaEntrataP(@RequestParam Long timeSheetGiornalieroId,@RequestParam LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetService.modificaEntrataPomeriggio(timeSheetGiornalieroId,oraModificata);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("Entrata Pomeriggio : modificata con successo");
    }

    @PostMapping("/uscitaPomeriggio")
    public ResponseEntity<?> modificaUscitaP(@RequestParam Long timeSheetGiornalieroId,@RequestParam LocalTime oraModificata)
    {
        TimeSheetGiornaliero giornaliero = timeSheetService.modificaUscitaPomeriggio(timeSheetGiornalieroId,oraModificata);
        timeSheetGiornalieroService.save(giornaliero);

        return ResponseEntity.ok("Uscita Pomeriggio : modificata con successo");
    }

        @GetMapping("/timbraturaLog")
    public List<StoricoTimbrature> leggiLog() {

        List<StoricoTimbrature> logs = storicoService.leggiLogs();
        return logs;
    }
}
