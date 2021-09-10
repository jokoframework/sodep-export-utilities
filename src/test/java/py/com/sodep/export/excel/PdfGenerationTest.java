package py.com.sodep.export.excel;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.itextpdf.text.DocumentException;

/**
 * Unit test for simple PDFGenerator.
 */
public class PdfGenerationTest extends TestCase {
	private static final String TEST_FILE_NAME = "grid.pdf";
	Logger log = Logger.getLogger(PdfGenerationTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public PdfGenerationTest(String testName) {
		super(testName);
		DOMConfigurator.configure("log4j.xml");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PdfGenerationTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws ConvertException
	 */
	public void testCreatePdfTable() throws FileNotFoundException,
			DocumentException, ConvertException {
		PdfGenerator pdfGenerator = new PdfGenerator();
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
		pdfGenerator.generateReportFile(data, header, TEST_FILE_NAME);

		log.info("PDF Created: " + TEST_FILE_NAME);
	}
}
