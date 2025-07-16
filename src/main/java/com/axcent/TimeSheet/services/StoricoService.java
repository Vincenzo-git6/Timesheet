package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.customHelper.LocalDateTimeForm;
import com.axcent.TimeSheet.entities.StoricoTimbrature;
import com.axcent.TimeSheet.repositories.StoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoricoService {

    private final StoricoRepository storicoRepository;
    private final CustomUtenteRepositoryImpl custom;

    public void stampaLog(String username, String azione, String data) {
        StoricoTimbrature log = new StoricoTimbrature();

        log.setUsername(username);
        log.setAzione(azione);
        log.setTimestamp(data);

        storicoRepository.save(log);
    }

    public List<StoricoTimbrature> leggiLogs() {
        return storicoRepository.findAll();
    }

    public Page<StoricoTimbrature> getStoricoPerGiorno(String giorno, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return storicoRepository.findByTimestampStartingWith(giorno, pageable);
    }
}
