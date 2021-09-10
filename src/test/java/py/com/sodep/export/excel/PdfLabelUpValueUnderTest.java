package py.com.sodep.export.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import py.com.sodep.export.exception.ConvertException;
import py.com.sodep.export.pdf.TableBuilderLabelUpValueUnder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

public class PdfLabelUpValueUnderTest extends TestCase{
	private static final String TEST_FILE_NAME = "gridCaptura.pdf";
	Logger log = Logger.getLogger(PdfLabelUpValueUnderTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public PdfLabelUpValueUnderTest(String testName) {
		super(testName);
		DOMConfigurator.configure("log4j.xml");
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PdfLabelUpValueUnderTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws ConvertException
	 */
	public void testCreatePdfTable() throws FileNotFoundException,
			DocumentException, ConvertException {
				//PdfGenerator pdfGenerator = new PdfGenerator();
				TableBuilderLabelUpValueUnder pdfGenerator= new TableBuilderLabelUpValueUnder();
				Image imagen=null;
				try {
					imagen = Image.getInstance("testgiod.jpg");
				} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}

				List<String> header = Arrays.asList("Name", "LastName", "Email", "Age",
						"Date of suscription" , "Pay", "Image");
				Table<Integer, Integer, Object> data = HashBasedTable.create();
				for (int i = 0; i < 40; i++) {
					data.put(i, 0, "Ale" + i);
					data.put(i, 1, "Feltes" + i);
					data.put(i, 2, "alefeltes@gmail.com" + i);
					data.put(i, 3, 38 + i);
					data.put(i, 4, new Date());
					data.put(i, 5, Boolean.TRUE);
					data.put(i, 6, imagen);
					
				}		
				
				pdfGenerator.generateReportFileImage(data, header, TEST_FILE_NAME, imagen);

				log.info("PDF Created: " + TEST_FILE_NAME);
		}
}


