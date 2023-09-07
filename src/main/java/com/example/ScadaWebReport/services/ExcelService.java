package com.example.ScadaWebReport.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.ScadaWebReport.Model.staticInfo.StaticInfoModel;

@Service
public class ExcelService {

	private String getCellValue(Cell cell) {
	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue();
	        case NUMERIC:
	            return String.valueOf((int) cell.getNumericCellValue());
	        default:
	            return "";
	    }
	}

	public List<StaticInfoModel> readExcelFile(InputStream filePath) throws IOException {
	    List<StaticInfoModel> staticInfoList = new ArrayList<>();

	    try (Workbook workbook = new XSSFWorkbook(filePath)) {

	        Sheet sheet = workbook.getSheetAt(0); // Предполагается, что данные находятся на первом листе
	        Iterator<Row> iterator = sheet.iterator();

	        
	        
	        while (iterator.hasNext()) {
	            Row currentRow = iterator.next();

	            // Пропустите первую строку (заголовки)
	            if (currentRow.getRowNum() < 3 ) {
	                continue;
	            }

	            Iterator<Cell> cellIterator = currentRow.iterator();
	            // Ходим по ячейкам и собираем данные из них
	            try {
	            	if(cellIterator.hasNext()) {
	                int id = parseCellValueToInt(getCellValue(cellIterator.next()));
	                System.out.print(id+" ");
	                if(id==0)
	                continue;
	                String name = getCellValue(cellIterator.next()).toString();
	                System.out.println(name);
	                String onlineId = getCellValue(cellIterator.next());
	                if(onlineId.equals(""))
	                	continue;
	                String levelId = getCellValue(cellIterator.next());
	                String totalId = getCellValue(cellIterator.next());
	                
	                String calibrationStatus = getCellValue(cellIterator.next());
	                String explanation = getCellValue(cellIterator.next());
	                String persent = getCellValue(cellIterator.next());
	                String cameraIp = getCellValue(cellIterator.next());
	                
	              

	                StaticInfoModel staticInfoModel = new StaticInfoModel(id, onlineId, totalId, levelId, name,
	                        persent, calibrationStatus, explanation, cameraIp);

	                staticInfoList.add(staticInfoModel);
	            	}
	            } catch (Exception e) {
	                // Логгирование ошибки
	                e.printStackTrace();
	            }
	        }
	    }

	    return staticInfoList;
	}



	private int parseCellValueToInt(String cellValue) {
	    try {
	        return Integer.parseInt(cellValue);
	    } catch (NumberFormatException e) {
	        return 0; // Значение по умолчанию в случае ошибки преобразования
	    }
	}
}