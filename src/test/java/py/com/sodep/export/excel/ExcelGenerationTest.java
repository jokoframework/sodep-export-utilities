package py.com.sodep.export.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Unit test for simple ExcelGenerator.
 */
public class ExcelGenerationTest extends TestCase {
	private static final String TEST_FILE_NAME = "workbook.xlsx";
	Logger log = Logger.getLogger(ExcelGenerationTest.class);
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");


	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public ExcelGenerationTest(String testName) {
		super(testName);
		DOMConfigurator.configure("log4j.xml");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ExcelGenerationTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testCreateSpreadsheet() {
		// 1. Create a new Workbook
		Workbook wb = new XSSFWorkbook();

		// 2. Create a new sheet
		Sheet sheet = wb.createSheet("sheet 1");

		// 3. Create a row
		Row row = sheet.createRow((short) 0);

		// 4. Create cells
		// 4.1 number cell
		row.createCell(0).setCellValue(1);
		// 4.2 text
		row.createCell(1).setCellValue("Text");
		// 4.3 date cell
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
				"m/d/yy h:mm"));
		Cell cell = row.createCell(2);
		cell.setCellValue(new Date());
		cell.setCellStyle(cellStyle);

		// 4.4 boolean cell
		row.createCell(3).setCellValue(true);

		// 5. create excel file
		FileOutputStream fileOut;
		try {

			fileOut = new FileOutputStream(TEST_FILE_NAME);
			wb.write(fileOut);
			fileOut.close();

		} catch (FileNotFoundException e) {
			log.error("", e);
			fail(e.getMessage());
		} catch (IOException e) {
			log.error("", e);
			fail(e.getMessage());
		}

		log.debug("File created!");
		assertTrue(new File(TEST_FILE_NAME).length() > 0);
	}

	public void testReadSpreadsheet() {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(new File(TEST_FILE_NAME));
		} catch (InvalidFormatException e) {
			log.error("", e);
			fail(e.getMessage());
		} catch (IOException e) {
			log.error("", e);
			fail(e.getMessage());
		}

		Sheet sheet = wb.getSheetAt(0);

		// *********************************
		Cell cell = sheet.getRow(0).getCell(0);
		Double numberVal = cell.getNumericCellValue();
		log.debug("Row: 0 - Column: 0 = " + numberVal);
		// -----------------------------
		cell = sheet.getRow(0).getCell(1);
		String stringVal = cell.getStringCellValue();
		log.debug("Row: 0 - Column: 1 = " + stringVal);
		// -----------------------------
		cell = sheet.getRow(0).getCell(2);
		Date dateVal = cell.getDateCellValue();
		log.debug("Row: 0 - Column: 2 = " + dateVal);
		// -----------------------------
		cell = sheet.getRow(0).getCell(3);
		Boolean booleanVal = cell.getBooleanCellValue();
		log.debug("Row: 0 - Column: 3 = " + booleanVal);
		// -----------------------------
		assertNotNull(numberVal != null && stringVal != null && dateVal != null
				&& booleanVal != null);
	}

	public void testCreateSpreadsheetFromData() {
        ExcelGenerator excelGenerator = new ExcelGenerator();
        Table<Integer, Integer, Object> data = HashBasedTable.create();
        List<String> header = Arrays.asList("Name", "LastName", "Email", "Age",
                "Date of suscription", "Willing to pay", "Integer", "BigDecimal", "BigInteger");
        for (int i = 0; i < 10; i++) {
            data.put(i, 0, "Ale" + i);
            data.put(i, 1, "Feltes" + i);
            data.put(i, 2, "alefeltes@gmail.com" + i);
            data.put(i, 3, 38 + i);
            data.put(i, 4, new Date());
            data.put(i, 5, Boolean.TRUE);
            data.put(i, 6, 456);
            data.put(i, 7, new BigDecimal("1000000000000.9876"));
            data.put(i, 8, new BigInteger("1000000000000"));
        }
		try {
			String fileName = TMP_DIR + File.separator + "Personal list version 1.xls";
			fileName = StringUtils.replace(fileName, " ", "_");
			log.debug("Escribiendo prueba a: "+ fileName);
			excelGenerator.generateExcel(data, header, fileName);
		} catch (ConvertException e) {
			log.error("", e);
			fail(e.getMessage());
		}
	}

	public void testFailErrorWithNumberString() {
		ExcelGenerator excelGenerator = new ExcelGenerator();
		List<String> header = Arrays.asList("Name", "LastName", "Email", "Age",
				"Date of suscription", "Willing to pay", "Text that looks like double", "Integer", "BigDecimal", "BigInteger");
		Table<Integer, Integer, Object> data = HashBasedTable.create();
		for (int i = 0; i < 10; i++) {
			data.put(i, 0, "Ale" + i);
			data.put(i, 1, "Feltes" + i);
			data.put(i, 2, "alefeltes@gmail.com" + i);
			data.put(i, 3, 38 + i);
			data.put(i, 4, new Date());
			data.put(i, 5, Boolean.TRUE);
            //Este fallaba en Captura, debido a la utilización del NumberUtils de common lang
			data.put(i, 6, "123L");
            data.put(i, 7, 456);
            data.put(i, 8, new BigDecimal("1000000000000.9876"));
            data.put(i, 9, new BigInteger("1000000000000"));
		}
		try {
            String fileName = TMP_DIR + File.separator + "Personal list version 2.xls";
			fileName = StringUtils.replace(fileName, " ", "_");
            log.debug("Escribiendo prueba a: "+ fileName);
            excelGenerator.generateExcel(data, header, fileName);
		} catch (ConvertException e) {
			log.error("", e);
			fail(e.getMessage());
		}
	}
}
