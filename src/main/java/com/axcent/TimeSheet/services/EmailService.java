package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TimeSheetGiornalieroService timeSheetGiornalieroService;

    /**
     * Invia una mail quando viene registrata un'assenza automatica
     * @param username nome utente (es. Mario Rossi)
     * @param email email del destinatario
     * @param data data dell'assenza
     * @param idTimeSheetGiornaliero id del timesheet giornaliero per il link di correzione
     */
    public void inviaEmailAssenza(String username, String email, LocalDate data, Long idTimeSheetGiornaliero) {
        System.out.println("[EmailService] Preparazione email assenza per utente " + username + " - email " + email);
        //TODO Mettere nome e cognome, metodo già fatto

        // Link aggiornato con id come query parameter
        String linkCorrezione = "http://localhost:4200/motivi-assenza?id=" + idTimeSheetGiornaliero;

        String oggetto = "Assenza automatica rilevata - " + data;
        String corpo = "Ciao " + username + ",\n\n" +
                "Oggi (" + data + ") risulti assente o con timbrature incomplete nel sistema TimeSheet.\n" +
                "È stata registrata un'assenza automatica.\n\n" +
                "Per favore accedi al portale e specifica il motivo cliccando su questo link:\n" +
                linkCorrezione + "\n\n" +
                "Grazie,\n" +
                "Il team HR";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(oggetto);
            message.setText(corpo);
            // message.setFrom("tuoindirizzo@gmail.com");

            System.out.println("[EmailService] Invio email a " + email + " con oggetto: " + oggetto);
            javaMailSender.send(message);
            System.out.println("[EmailService] Email inviata con successo a " + email);

            log.info("Email inviata con successo a {}", email);

        } catch (Exception ex) {
            System.err.println("[EmailService] ERRORE nell'invio dell'email a " + email + ": " + ex.getMessage());
            log.error("Errore nell'invio dell'email a {}: {}", email, ex.getMessage(), ex);
        }
    }

    public void inviaEmailUscitaNonTimbrata(String username, String email, LocalDate data, Long idTimeSheetGiornaliero) {
        TimeSheetGiornaliero timeSheetGiornaliero = timeSheetGiornalieroService.findById(idTimeSheetGiornaliero);

        boolean mattinaMancante = timeSheetGiornaliero.getUscitaMattina() == null;
        boolean pomeriggioMancante = timeSheetGiornaliero.getUscitaPomeriggio() == null;

        String periodoMancante = null;

        // La logica è stata invertita per verificare prima il caso più specifico (entrambi mancanti)
        if (mattinaMancante && pomeriggioMancante) {
            periodoMancante = "delle uscite della mattina e del pomeriggio";
        } else if (mattinaMancante) {
            periodoMancante = "dell'uscita di questa mattina";
        } else if (pomeriggioMancante) {
            periodoMancante = "dell'uscita di questo pomeriggio";
        }

        if (periodoMancante != null) {
            System.out.println("[EmailService] Preparazione email uscita non timbrata per utente " + username + " - email " + email);

            String oggetto = "Uscita non timbrata rilevata - " + data;
            // Usiamo String.format per una migliore leggibilità
            String corpo = String.format(
                    "Ciao %s,\n\n" +
                            "In data %s risulta mancante la timbratura %s nel sistema TimeSheet.\n" +
                            "Per favore contatta il team HR e chiedi di modificare l'orario di uscita, specificando data e ora.\n\n" +
                            "Grazie,\n" +
                            "Il team HR",
                    username, data, periodoMancante
            );

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(oggetto);
                message.setText(corpo);

                System.out.println("[EmailService] Invio email a " + email + " con oggetto: " + oggetto);
                javaMailSender.send(message);
                System.out.println("[EmailService] Email inviata con successo a " + email);
                log.info("Email inviata con successo a {}", email);

            } catch (Exception ex) {
                System.err.println("[EmailService] ERRORE nell'invio dell'email a " + email + ": " + ex.getMessage());
                log.error("Errore nell'invio dell'email a {}: {}", email, ex.getMessage(), ex);
            }
        } 
    }
}
