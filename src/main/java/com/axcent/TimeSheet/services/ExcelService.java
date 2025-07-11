package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class ExcelService {

    public byte[] generateTimesheetExcel(TimeSheetMensile mensile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RILEVAZIONE PRESENZE");

            // -- STILE --
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // -- INTESTAZIONI (come nello screenshot) --
            Row r1 = sheet.createRow(0);
            r1.createCell(0).setCellValue("AXCENT");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 2));

            Row r2 = sheet.createRow(1);
            r2.createCell(0).setCellValue("RILEVAZIONE PRESENZE");

            Row r3 = sheet.createRow(2);
            r3.createCell(0).setCellValue("Nome e Cognome:");
            r3.createCell(1).setCellValue(mensile.getNome() + " " + mensile.getCognome());

            r3.createCell(3).setCellValue("Anno");
            r3.createCell(4).setCellValue(mensile.getAnno());

            r3.createCell(5).setCellValue("Mese");
            r3.createCell(6).setCellValue(mensile.getMese());

            Row r4 = sheet.createRow(3);
            r4.createCell(0).setCellValue("Sede e/o Cantiere:");
            r4.createCell(1).setCellValue(mensile.getSede());

            // -- TABELLA HEADER --
            Row header = sheet.createRow(5);
            String[] intestazioni = {
                    "Giorno", "Data", "I_M", "U_M", "I_P", "U_P", "I_S", "U_S",
                    "Totale ore", "Motivo"
            };
            for (int i = 0; i < intestazioni.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(intestazioni[i]);
                cell.setCellStyle(headerStyle);
            }

            // -- RIGHE GIORNALIERE --
            int rowIdx = 6;
            DateTimeFormatter giornoFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ITALIAN);
            DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM");

            for (TimeSheetGiornaliero g : mensile.getGiornalieri()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(g.getData().format(giornoFormatter));
                row.createCell(1).setCellValue(g.getData().format(dataFormatter));

                row.createCell(2).setCellValue(g.getEntrataMattina() != null ? g.getEntrataMattina().toString() : "");
                row.createCell(3).setCellValue(g.getUscitaMattina() != null ? g.getUscitaMattina().toString() : "");
                row.createCell(4).setCellValue(g.getEntrataPomeriggio() != null ? g.getEntrataPomeriggio().toString() : "");
                row.createCell(5).setCellValue(g.getUscitaPomeriggio() != null ? g.getUscitaPomeriggio().toString() : "");
                row.createCell(6).setCellValue(g.getEntrataStraordinario() != null ? g.getEntrataStraordinario().toString() : "");
                row.createCell(7).setCellValue(g.getUscitaStraordinario() != null ? g.getUscitaStraordinario().toString() : "");

                row.createCell(8).setCellValue(g.getOreFormattate());
                row.createCell(9).setCellValue(g.getMotivo() != null ? g.getMotivo().name() : "");
            }

            for (int i = 0; i <= 9; i++) {
                sheet.autoSizeColumn(i);
            }

            // -- OUTPUT STREAM --
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}

