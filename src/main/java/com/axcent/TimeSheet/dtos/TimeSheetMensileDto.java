package com.axcent.TimeSheet.dtos;

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

    private List<TimeSheetGiornalieroDto> giornalieri;

    public TimeSheetMensileDto(TimeSheetMensile entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.nome = entity.getNome();
        this.cognome = entity.getCognome();
        this.sede = entity.getSede();
        this.anno = entity.getAnno();
        this.mese = entity.getMese();
        this.giornalieri = entity.getGiornalieri().stream()
                .map(TimeSheetGiornalieroDto::new)
                .collect(Collectors.toList());
    }
}
