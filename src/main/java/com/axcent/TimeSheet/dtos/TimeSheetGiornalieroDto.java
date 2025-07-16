package com.axcent.TimeSheet.dtos;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetGiornalieroDto {

    private String data;
    private String entrataMattina;
    private String uscitaMattina;
    private String entrataPomeriggio;
    private String uscitaPomeriggio;
    private String entrataStraordinario;
    private String uscitaStraordinario;
    private String totaleOre;
    private String motivo;

    public TimeSheetGiornalieroDto(TimeSheetGiornaliero g) {
        this.data = g.getDataFormattata(); // es. "Martedì 15/07"
        this.entrataMattina = g.getEntrataMattinaFormattata();
        this.uscitaMattina = g.getUscitaMattinaFormattata();
        this.entrataPomeriggio = g.getEntrataPomeriggioFormattata();
        this.uscitaPomeriggio = g.getUscitaPomeriggioFormattata();
        this.entrataStraordinario = g.getEntrataStraordinarioFormattata();
        this.uscitaStraordinario = g.getUscitaStraordinarioFormattata();
        this.totaleOre = g.getOreFormattate();
        this.motivo = g.getMotivo() != null ? g.getMotivo().name() : "";
    }
}

