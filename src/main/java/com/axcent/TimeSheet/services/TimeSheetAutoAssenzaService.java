package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.entities.customHelper.LocalDateTimeForm;
import com.axcent.TimeSheet.entities.enums.Motivo;
import com.axcent.TimeSheet.repositories.TimeSheetGiornalieroRepository;
import com.axcent.TimeSheet.repositories.customs.CustomUtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSheetAutoAssenzaService {

    private final TimeSheetMensileService mensileService;
    private final TimeSheetGiornalieroService giornalieroService;
    private final TimeSheetGiornalieroRepository giornalieroRepository;
    private final CustomUtenteRepository customUtenteRepository;
    private final EmailService emailService;
    private final StoricoService storicoService;

    @Scheduled(cron = "0 0 12 * * *") // ogni giorno alle 12:00
    public void controllaAssenzePerTuttiGliUtenti() {
        LocalDate oggi = LocalDate.now();
        int mese = oggi.getMonthValue();
        int anno = oggi.getYear();

        List<TimeSheetMensile> mensili = mensileService.findAllTimesheet();

        for (TimeSheetMensile mensile : mensili) {
            if (mensile.getMese() != mese || mensile.getAnno() != anno) {
                continue;
            }

            Long userId = mensile.getUserId();
            String username = customUtenteRepository.getUtenteUsername(userId);
            String email = customUtenteRepository.getEmailByUserId(userId);

            TimeSheetGiornaliero giornaliero = giornalieroService.createOrFindTimeSheetG(mensile);

            boolean haTimbrato = giornaliero.getEntrataMattina() != null || giornaliero.getUscitaMattina() != null ||
                    giornaliero.getEntrataPomeriggio() != null || giornaliero.getUscitaPomeriggio() != null;

            boolean haTimbratoSoloUnaParte = (
                    (giornaliero.getEntrataMattina() != null || giornaliero.getUscitaMattina() != null) ^
                            (giornaliero.getEntrataPomeriggio() != null || giornaliero.getUscitaPomeriggio() != null)
            );

            if (giornaliero.getMotivo() == null && (!haTimbrato || haTimbratoSoloUnaParte)) {

                giornaliero.setMotivo(Motivo.ASSENZA);
                giornalieroRepository.save(giornaliero);

                storicoService.stampaLog(username, "Assenza automatica registrata", LocalDateTimeForm.now());

                emailService.inviaEmailAssenza(username, email, oggi, giornaliero.getId());
            }
        }
    }
}
