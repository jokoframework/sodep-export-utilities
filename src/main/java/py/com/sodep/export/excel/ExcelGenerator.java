package py.com.sodep.export.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import py.com.sodep.export.TabularReport;
import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.Table;

/**
 * Excel Generator
 * 
 */
public class ExcelGenerator implements TabularReport {

	static Logger log = Logger.getLogger(ExcelGenerator.class);

	public void generateExcel(Table<Integer, Integer, Object> data,
			List<String> header, String fileName) throws ConvertException {
		generateReportFile(data, header, fileName);
	}

	@Override
	public void generateReportFile(Table<Integer, Integer, Object> data,
			List<String> header, String fileName) throws ConvertException {
		int rowCount = 0;
		// 1. Create a new Workbook
		Workbook wb = new XSSFWorkbook();
		// 2. Create a new sheet
		Sheet sheet = wb.createSheet("sheet 1");
		Object registro = null;
		if (!data.isEmpty()) {
			Row row = sheet.createRow(rowCount);
			for (int i = 0; i < header.size(); i++) {
				// 3. Create a header row
				row.createCell(i).setCellValue(header.get(i));
			}
		} else {
			throw new ConvertException("Empty data set");
		}

		Iterator<Integer> rows = data.rowKeySet().iterator();
		while (rows.hasNext()) {
			rowCount += 1;
			Row row = sheet.createRow(rowCount);
			Integer rowNumber = rows.next();
			Map<Integer, Object> currentRow = data.row(rowNumber);
			Iterator<Integer> rowDataIterator = currentRow.keySet().iterator();
			while (rowDataIterator.hasNext()) {
				Integer colNumber = rowDataIterator.next();
				registro = currentRow.get(colNumber);
				// TODO: Format according cell type
				if (registro instanceof Number) {
					row.createCell(colNumber).setCellValue(
							Double.parseDouble(registro.toString()));
					log.debug("Number: " + registro);
				} else if (registro instanceof Date) {
					row.createCell(colNumber).setCellValue((Date) registro);
					log.debug("Date: " + registro);
				} else if (registro instanceof Boolean) {
					row.createCell(colNumber).setCellValue((Boolean) registro);
					log.debug("Boolean: " + registro);
				} else {
					row.createCell(colNumber).setCellValue(registro.toString());
					log.debug("String: " + registro);
				}
			}// End column iteration
		}// End row iteration
			// 5. create excel file
		FileOutputStream fileOut;
		try {

			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();

		} catch (FileNotFoundException e) {
			throw new ConvertException(e);
		} catch (IOException e) {
			throw new ConvertException();
		}
	}
}
