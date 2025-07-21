package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.dtos.TimeSheetMensileDto;
import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.services.CustomUtenteRepositoryImpl;
import com.axcent.TimeSheet.services.ExcelService;
import com.axcent.TimeSheet.services.TimeSheetGiornalieroService;
import com.axcent.TimeSheet.services.TimeSheetMensileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.atp.Switch;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExcelController {

    private final ExcelService excelService;
    private final TimeSheetMensileService timeSheetMensileService;
    private final CustomUtenteRepositoryImpl custom;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTimesheet(@PathVariable Long id) throws IOException {
        TimeSheetMensile mensile = timeSheetMensileService.getById(id);

        byte[] excelBytes = excelService.generateTimesheetExcel(mensile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timesheet_" + id + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/user/{userId}")
    public List<TimeSheetMensileDto> listAll(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer anno)
    {
        return timeSheetMensileService.getByUserIdAndAnnoDto(userId, anno);
    }

    //in uso
    @GetMapping("/download/{anno}/{mese}")
    public ResponseEntity<byte[]> download(
            HttpServletRequest request,
            @PathVariable int anno,
            @PathVariable int mese)
    {
        try {
            Long userId = (Long) request.getAttribute("userId");

            byte[] file = timeSheetMensileService.generateExcelByUserAnnoEMese(userId, anno, mese);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData(
                    "attachment", "timesheet_" + anno + "_" + mese + ".xlsx");
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        } catch (NoSuchElementException | IOException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    //in uso
    //prende tutti i timesheet disponibili in base all'anno
    @GetMapping("/anno/{anno}")
    public List<TimeSheetMensileDto> getMensiliPerAnno(@PathVariable int anno, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        List<TimeSheetMensile> mensili = timeSheetMensileService.getMensiliByUserIdAndAnno(userId, anno);

        return mensili.stream()
                .map(m -> new TimeSheetMensileDto(m, m.getGiornalieri()))
                .collect(Collectors.toList());
    }

    @GetMapping("/timesheets")
    public TimeSheetMensileDto getTimesheetUtente(
            @RequestParam String username,
            @RequestParam int anno,
            @RequestParam int mese)
    {
        Long userId = custom.findUserIdByUsername(username);
        TimeSheetMensile mensile = timeSheetMensileService.getMensiliByUserIdAndAnnoAndMese(userId, anno, mese);

        if (mensile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timesheet non trovato");
        }

        return new TimeSheetMensileDto(mensile, mensile.getGiornalieri());
    }



}

