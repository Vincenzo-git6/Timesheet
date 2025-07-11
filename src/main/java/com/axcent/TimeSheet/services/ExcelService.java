package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.entities.TimeSheetMensile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream exportTimeSheetsToExcel(List<TimeSheetMensile> timeSheetsmensile) throws IOException
    {
        String[] columns = {"Nome e cognome","Sede","Anno","Mese","Data", "I/M", "U/M","I/P","U/P","I/S","U/S"};
    }
}
