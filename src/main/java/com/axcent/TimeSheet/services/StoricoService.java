package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.repositories.StoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoricoService {

    private final StoricoRepository storicoRepository;
    private final CustomUtenteRepositoryImpl custom;

    public void stampaLog(String username, String azione, LocalDate data) {
        StoricoTimbrature log = new StoricoTimbrature();

        log.setUsername(username);
        log.setAzione(azione);
        log.setTimestamp(data);

        storicoRepository.save(log);
    }

    public List<StoricoTimbrature> leggiLogs() {
        return storicoRepository.findAll();
    }
}
