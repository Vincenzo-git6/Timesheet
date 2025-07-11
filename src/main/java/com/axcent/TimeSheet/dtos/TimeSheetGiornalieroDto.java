package com.axcent.TimeSheet.dtos;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetGiornalieroDto {
    private Long id;
    private String data;
    private String giornoSettimana;
    private LocalTime entrataMattina;
    private LocalTime uscitaMattina;
    private LocalTime entrataPomeriggio;
    private LocalTime uscitaPomeriggio;
    private LocalTime entrataStraordinario;
    private LocalTime uscitaStraordinario;

    private String motivo;

    private String oreLavorate;      // es. "08:45"
    private String dataFormattata;   // es. "Lunedì 08/07"

    public TimeSheetGiornalieroDto(TimeSheetGiornaliero entity) {
        this.id = entity.getId();
        this.data = entity.getData().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        this.giornoSettimana = entity.getData().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("it","IT"));
        this.entrataMattina = entity.getEntrataMattina();
        this.uscitaMattina = entity.getUscitaMattina();
        this.entrataPomeriggio = entity.getEntrataPomeriggio();
        this.uscitaPomeriggio = entity.getUscitaPomeriggio();
        this.entrataStraordinario = entity.getEntrataStraordinario();
        this.uscitaStraordinario = entity.getUscitaStraordinario();
        this.motivo = entity.getMotivo() != null ? entity.getMotivo().name() : null;
        this.oreLavorate = entity.getOreFormattate();
        this.dataFormattata = entity.getDataFormattata();
    }
}
