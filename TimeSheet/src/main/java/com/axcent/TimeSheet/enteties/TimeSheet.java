package com.axcent.TimeSheet.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String nome;
    private String cognome;
    private String sede;
    private Date data;
    private LocalTime entrataMattina;
    private LocalTime uscitaMattina;
    private LocalTime uscitaPomeriggio;
    private LocalTime entrataPomeriggio;
    private LocalTime entrataStraordinario;
    private LocalTime uscitaStraordinario;
    private Motivo motivo;

    public Duration getOreLavorate()
    {
        Duration mattina = Duration.ZERO;
        Duration pomeriggio = Duration.ZERO;
        Duration straordinario = Duration.ZERO;

        if(entrataMattina != null && uscitaPomeriggio!= null)
            mattina= Duration.between(entrataMattina,uscitaMattina);

        if(entrataPomeriggio != null && uscitaPomeriggio!= null)
            pomeriggio= Duration.between(entrataPomeriggio,uscitaPomeriggio);

        if(entrataStraordinario!=null && uscitaStraordinario!=null)
            straordinario = Duration.between(entrataStraordinario,uscitaStraordinario);

        return mattina.plus(pomeriggio).plus(straordinario);

    }

    @Transient
    public String getOreFormattate()
    {
        Duration totale = getOreLavorate();
        long ore = totale.toHours();
        long minuti = totale.toMinutesPart();

        return  String.format("%02D:%02D",ore,minuti);
    }

}
