package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.repositories.StoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoricoController {

    private final StoricoRepository storicoRepo;


    @GetMapping("/api/storico-timbrature")
    public Page<StoricoTimbrature> getStoricoPerData(
              @RequestParam String data,
            @RequestParam int page,
            @RequestParam int size) {

        // Filtra per data (esempio con LIKE su timestamp, adattalo se serve)
        Pageable pageable = PageRequest.of(page, size);

        // Supponendo che timestamp sia una stringa con formato "dd/MM/yyyy HH:mm"
        // Devi filtrare solo per la parte della data (dd/MM/yyyy)
        String dataFormattata = data; // esempio "16/07/2025"
        return storicoRepo.findByTimestampStartingWith(dataFormattata, pageable);
    }
}
