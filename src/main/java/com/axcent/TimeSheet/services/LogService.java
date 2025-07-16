package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimbraturaLog;
import com.axcent.TimeSheet.repositories.TimbraturaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogService {

    private final TimbraturaLogRepository timbraturaLogRepository;
    private final CustomUtenteRepositoryImpl custom;

    public void stampaLog(String username, String azione, LocalDate data) {
        TimbraturaLog log = new TimbraturaLog();

        log.setUsername(username);
        log.setAzione(azione);
        log.setTimestamp(data);

        timbraturaLogRepository.save(log);
    }

    public List<TimbraturaLog> leggiLogs() {
        return timbraturaLogRepository.findAll();
    }
}
