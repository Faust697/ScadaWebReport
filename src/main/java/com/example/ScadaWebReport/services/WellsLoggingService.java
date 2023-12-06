package com.example.ScadaWebReport.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WellsLoggingService {

	ArrayList<Well> wells;
	static boolean fileExist = false;

	@Autowired
	public WellsLoggingService(ArrayList<Well> wells) {
		super();
		this.wells = wells;
	}

	ArrayList<String> wellNames = new ArrayList<>();
	ArrayList<String> values = new ArrayList<>();

	public void fillinfLists(ArrayList<Well> wells)
	{
		wellNames.add(0, "Dates");
		values.add(0, getFormattedDate());

		for (Well well : wells) {
			wellNames.add(well.getName());
			values.add(well.getTotalFlow());
		}
		
	}
	
	public void Logg() throws IOException {

		fillinfLists(wells);
		
		// Генерация имени файла
		String fileName = generateFileName();
		XSSFWorkbook workbook = createWorkbook(fileName);
		XSSFSheet sheet = null;
		Map<String, ArrayList<String>> NamesMap = new TreeMap<String, ArrayList<String>>();
		sheet = workbook.getSheetAt(0);

		// Если файла нет
		if (fileExist == false) {

			NamesMap.put("1", wellNames);
			NamesMap.put("2", values);
			keySetToCells(sheet, NamesMap, 0);

		}
		// Если файл есть
		else {

			NamesMap.put("2", values);
			keySetToCells(sheet, NamesMap, getLastRow(fileName));
		}

		// Запись в файл
		try {
			FileOutputStream out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
			System.out.println(getFormattedDate()+": "+fileName + " written successfully on disk.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Разбивка мепа на ячейки(так же откуда начинать запись)
	private void keySetToCells(Sheet sheet, Map<String, ArrayList<String>> map, int rowNum) {

		Set<String> keyset = map.keySet();

		int rownum = rowNum;// откуда начинаем запись.
		//boolean isDate = true;
		for (String key : keyset) {

			Row row = sheet.createRow(rownum++);
			ArrayList<String> objArr = map.get(key);
			int cellnum = 0;

			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String) {

					if (obj.equals(""))
						obj = "0";

					cell.setCellValue((String) obj);
				} else if (obj instanceof Integer) {

					cell.setCellValue((Integer) obj);
				}
			}
		}

	}

//Последний свободный ряд
	public int getLastRow(String name) throws IOException {
		int lastColumnNumber = 0;
		FileInputStream file = new FileInputStream(new File(name));

		try (// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file)) {
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			Row row = sheet.getRow(0);

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				lastColumnNumber++;
			}
		}

		// For each row, iterate through all the columns
		/*
		 * Iterator<Cell> cellIterator = row.cellIterator();
		 * 
		 * while (cellIterator.hasNext()) {
		 * 
		 * Cell cell = cellIterator.next();
		 * 
		 * }
		 */

		file.close();
		return lastColumnNumber;

	}

	
	//Создание воркбука
	private static XSSFWorkbook createWorkbook(String fileName) throws IOException {
		XSSFWorkbook workbook = null;
		File f = new File(fileName);
		long creationTimeMillis = f.lastModified();

		// Преобразуем миллисекунды в объект Date
		Date creationDate = new Date(creationTimeMillis);

		if (f.exists() && f.length() > 0 && isToday(creationDate)) {
			try {
				FileInputStream file = new FileInputStream(new File(fileName));
				workbook = new XSSFWorkbook(file);
				fileExist = true;

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Файл не существует или пуст");
			// Если файл не существует или пуст, создаем новый Workbook
			workbook = new XSSFWorkbook();
			// Если книга новая, добавляем в нее новый лист
			workbook.createSheet();
			fileExist = false;
		}

		return workbook;
	}
   //Проверка, сегодня ли создан файл
	private static boolean isToday(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(new Date());
		String dateStr = dateFormat.format(date);
		return today.equals(dateStr);
	}
	
	//Генерация названия файла
	private static String generateFileName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateStr = now.format(formatter);
		return "SCADA_WELL_" + dateStr + ".xlsx";
	}

	
	private static String getFormattedDate() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}
}
