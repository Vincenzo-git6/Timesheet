package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetGiornaliero;
import com.axcent.TimeSheet.entities.TimeSheetMensile;
import com.axcent.TimeSheet.repositories.TimeSheetMensileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final TimeSheetMensileRepository timeSheetMensileRepository;

    public byte[] generateTimesheetExcel(TimeSheetMensile mensile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("RILEVAZIONE PRESENZE");

            // Font base
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            // Stile intestazione tabella
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(boldFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Stile celle dati
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Titolo principale
            Row r1 = sheet.createRow(0);
            Cell titleCell = r1.createCell(0);
            titleCell.setCellValue("RILEVAZIONE PRESENZE");
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));

            // Info personale
            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("Nome e Cognome:");
            r2.createCell(1).setCellValue(mensile.getNome() + " " + mensile.getCognome());
            r2.createCell(3).setCellValue("Anno:");
            r2.createCell(4).setCellValue(mensile.getAnno());
            r2.createCell(5).setCellValue("Mese:");
            r2.createCell(6).setCellValue(mensile.getMese());

            Row r3 = sheet.createRow(3);
            r3.createCell(0).setCellValue("Sede e/o Cantiere:");
            r3.createCell(1).setCellValue(mensile.getSede());

            // Header della tabella
            Row header = sheet.createRow(5);
            String[] intestazioni = {
                    "Giorno", "Data", "I_M", "U_M", "I_P", "U_P", "I_S", "U_S", "Totale ore", "Motivo"
            };
            for (int i = 0; i < intestazioni.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(intestazioni[i]);
                cell.setCellStyle(headerStyle);
            }

            // Righe giornaliere
            int rowIdx = 6;
            DateTimeFormatter giornoFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ITALIAN);
            DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM");

            for (TimeSheetGiornaliero g : mensile.getGiornalieri()) {
                Row row = sheet.createRow(rowIdx++);

                // Colonna Giorno e Data
                Cell giornoCell = row.createCell(0);
                giornoCell.setCellValue(g.getData().format(giornoFormatter));
                giornoCell.setCellStyle(dataStyle);

                Cell dataCell = row.createCell(1);
                dataCell.setCellValue(g.getData().format(dataFormatter));
                dataCell.setCellStyle(dataStyle);

                // Colonne orari
                row.createCell(2).setCellValue(formatOrEmpty(g.getEntrataMattina()));
                row.createCell(3).setCellValue(formatOrEmpty(g.getUscitaMattina()));
                row.createCell(4).setCellValue(formatOrEmpty(g.getEntrataPomeriggio()));
                row.createCell(5).setCellValue(formatOrEmpty(g.getUscitaPomeriggio()));
                row.createCell(6).setCellValue(formatOrEmpty(g.getEntrataStraordinario()));
                row.createCell(7).setCellValue(formatOrEmpty(g.getUscitaStraordinario()));
                row.createCell(8).setCellValue(g.getOreFormattate());
                row.createCell(9).setCellValue(g.getMotivo() != null ? g.getMotivo().name() : "");

                for (int i = 2; i <= 9; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Auto-size colonne
            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String formatOrEmpty(LocalTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }

}
