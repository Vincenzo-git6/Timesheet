package com.axcent.TimeSheet.dtos;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetMensileDto {

    private Long id;
    private Long userId;
    private String nome;
    private String cognome;
    private String sede;
    private int anno;
    private int mese;

    private List<TimeSheetGiornalieroDto> giorni;

    public TimeSheetMensileDto(TimeSheetMensile mensile, List<TimeSheetGiornaliero> giornalieri) {
        this.id = mensile.getId();
        this.userId = mensile.getUserId();
        this.nome = mensile.getNome();
        this.cognome = mensile.getCognome();
        this.sede = mensile.getSede();
        this.anno = mensile.getAnno();
        this.mese = mensile.getMese();
        this.giorni = giornalieri.stream().map(TimeSheetGiornalieroDto::new).collect(Collectors.toList());
    }
}
