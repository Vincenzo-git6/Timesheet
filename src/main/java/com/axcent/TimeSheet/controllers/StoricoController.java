package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.repositories.StoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StoricoController {

    private final StoricoRepository storicoRepository;


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
}
