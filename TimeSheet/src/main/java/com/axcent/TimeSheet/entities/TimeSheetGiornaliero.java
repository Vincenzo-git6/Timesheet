package com.axcent.TimeSheet.entities;

import com.axcent.TimeSheet.entities.enums.Motivo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetGiornaliero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @Column(name = "I_M")
    private LocalTime entrataMattina;

    @Column(name = "U_M")
    private LocalTime uscitaMattina;

    @Column(name = "I_P")
    private LocalTime entrataPomeriggio;

    @Column(name = "U_P")
    private LocalTime uscitaPomeriggio;

    @Column(name = "I_S")
    private LocalTime entrataStraordinario;

    @Column(name = "U_S")
    private LocalTime uscitaStraordinario;

    @Enumerated(EnumType.STRING)
    private Motivo motivo;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private TimeSheetMensile timesheetMensile;

    @Transient
    public Duration getOreLavorate() {
        Duration mattina = (entrataMattina != null && uscitaMattina != null)
                ? Duration.between(entrataMattina, uscitaMattina)
                : Duration.ZERO;

        Duration pomeriggio = (entrataPomeriggio != null && uscitaPomeriggio != null)
                ? Duration.between(entrataPomeriggio, uscitaPomeriggio)
                : Duration.ZERO;

        Duration straordinario = (entrataStraordinario != null && uscitaStraordinario != null)
                ? Duration.between(entrataStraordinario, uscitaStraordinario)
                : Duration.ZERO;

        return mattina.plus(pomeriggio).plus(straordinario);
    }

    @Transient
    public String getOreFormattate() {
        Duration totale = getOreLavorate();
        long ore = totale.toHours();
        long minuti = totale.toMinutesPart();
        return String.format("%02d:%02d", ore, minuti);
    }

    @Transient
    public String getDataFormattata() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM", Locale.ITALIAN);
        return data != null ? data.format(formatter) : "";
    }
}
