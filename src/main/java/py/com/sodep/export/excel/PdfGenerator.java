package py.com.sodep.export.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import py.com.sodep.export.TabularReport;
import py.com.sodep.export.exception.ConvertException;
import py.com.sodep.export.pdf.TableBuilder;

import com.google.common.collect.Table;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Pdf Generator
 * 
 */
public class PdfGenerator implements TabularReport {

	static Logger log = Logger.getLogger(PdfGenerator.class);

	private String title;

	public PdfGenerator() {
	}

	public PdfGenerator(String title) {
		setTitle(title);
	}

	@Override
	public void generateReportFile(Table<Integer, Integer, Object> data,
			List<String> header, String testFileName) throws ConvertException {
		// step 1
		Document document = new Document();
		document.setPageSize(PageSize.A4);

		// step 2
		try {
			TableBuilder tableBuilder = new TableBuilder(getTitle());
			PdfWriter.getInstance(document, new FileOutputStream(testFileName));

			// step 3
			document.open();

			// step 4 create PDF content
			document.add(tableBuilder.createTable(header, data));

			// step 5
			document.close();
		} catch (FileNotFoundException e) {
			throw new ConvertException(e);
		} catch (DocumentException e) {
			throw new ConvertException(e);

		}

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
