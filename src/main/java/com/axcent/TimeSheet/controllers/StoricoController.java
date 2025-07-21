package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.repositories.StoricoRepository;
import com.axcent.TimeSheet.services.StoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StoricoController {

    private final StoricoRepository storicoRepository;
    private final StoricoService storicoService;


    @GetMapping("/api/storico-timbrature")
    public Page<StoricoTimbrature> getStoricoPerData(
            @RequestParam String data,
            @RequestParam int page,
            @RequestParam int size) {

        // Filtra per data
        Pageable pageable = PageRequest.of(page, size);

        String dataFormattata = data; // esempio "16/07/2025"
        return storicoRepository.findByTimestampStartingWith(dataFormattata, pageable);
    }

    @GetMapping("/timbraturaLog")
    public List<StoricoTimbrature> leggiLog() {

        List<StoricoTimbrature> logs = storicoService.leggiLogs();
        return logs;
    }
}
