package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.services.ExcelService;
import com.axcent.TimeSheet.services.TimeSheetMensileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final TimeSheetMensileService timeSheetMensileService; // o il tuo servizio

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTimesheet(@PathVariable Long id) throws IOException {
        TimeSheetMensile mensile = timeSheetMensileService.getById(id);

        byte[] excelBytes = excelService.generateTimesheetExcel(mensile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timesheet_" + id + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}

