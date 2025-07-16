package com.axcent.TimeSheet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatoTimbraturaDto
{
    private boolean entrataMattina;
    private boolean uscitaMattina;
    private boolean entrataPomeriggio;
    private boolean uscitaPomeriggio;
    private boolean entrataStraordinario;
    private boolean uscitaStraordinario;
    private boolean assenzaSegnata;
}
