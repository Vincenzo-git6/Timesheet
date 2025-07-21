package com.axcent.TimeSheet.entities;

import com.axcent.TimeSheet.entities.enums.Motivo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @JsonDeserialize(using = MotivoDeserializer.class)
    private Motivo motivo;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    @JsonBackReference
    private TimeSheetMensile timesheetMensile;

    @Transient
    @JsonIgnore
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
    @JsonIgnore
    public String getOreFormattate() {
        Duration totale = getOreLavorate();
        long ore = totale.toHours();
        long minuti = totale.toMinutesPart();
        return String.format("%02d:%02d", ore, minuti);
    }

    @Transient
    @JsonIgnore
    public String getDataFormattata() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM", Locale.ITALIAN);
        return data != null ? data.format(formatter) : "";
    }

    @Transient
    @JsonIgnore
    private String formatOrEmpty(LocalTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }

    @Transient
    @JsonIgnore
    public String getEntrataMattinaFormattata() {
        return formatOrEmpty(entrataMattina);
    }

    @Transient
    @JsonIgnore
    public String getUscitaMattinaFormattata() {
        return formatOrEmpty(uscitaMattina);
    }

    @Transient
    @JsonIgnore
    public String getEntrataPomeriggioFormattata() {
        return formatOrEmpty(entrataPomeriggio);
    }

    @Transient
    @JsonIgnore
    public String getUscitaPomeriggioFormattata() {
        return formatOrEmpty(uscitaPomeriggio);
    }

    @Transient
    @JsonIgnore
    public String getEntrataStraordinarioFormattata() {
        return formatOrEmpty(entrataStraordinario);
    }

    @Transient
    @JsonIgnore
    public String getUscitaStraordinarioFormattata() {
        return formatOrEmpty(uscitaStraordinario);
    }

}
