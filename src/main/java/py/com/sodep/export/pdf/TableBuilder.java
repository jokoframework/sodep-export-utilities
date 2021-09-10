package py.com.sodep.export.pdf;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.Table;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class TableBuilder {
	static Logger log = Logger.getLogger(TableBuilder.class);

	private String title;

	public TableBuilder(String title) {
		setTitle(title);
	}

	// create table
	public PdfPTable createTable(List<String> header,
			Table<Integer, Integer, Object> data) throws DocumentException,
			ConvertException {

		int columnCount = data.columnKeySet().size();
		PdfPTable table = new PdfPTable(columnCount);

		// set the width of the table to 100% of page
		table.setWidthPercentage(100);

		float[] columnWidth = new float[columnCount];
		for (int i = 0; i < columnCount; i++)
			columnWidth[i] = (float) (1f / columnCount);
		// set relative columns width
		table.setWidths(columnWidth);

		table.setHeaderRows(1);

		if (StringUtils.isNotBlank(getTitle())) {
			// ----------------Table Header "Title"----------------
			// font
			Font font = new Font(FontFamily.HELVETICA, 14, Font.BOLD,
					BaseColor.WHITE);
			// create header cell
			PdfPCell cell = new PdfPCell(new Phrase(getTitle(), font));
			
			cell.setColspan(columnCount);
			
			// set style
			Style.headerCellStyle(cell);
			
			// add to table
			table.addCell(cell);
		}

		// -----------------Table Cells Label/Value------------------

		Object registro = null;
		if (!data.isEmpty()) {
			for (int i = 0; i < header.size(); i++) {
				// 3. Create a header row
				table.addCell(createLabelCell(header.get(i)));
			}
		} else {
			throw new ConvertException("Empty data set");
		}

		Iterator<Integer> rows = data.rowKeySet().iterator();
		while (rows.hasNext()) {
			Integer rowNumber = rows.next();
			Map<Integer, Object> currentRow = data.row(rowNumber);
			Iterator<Integer> rowDataIterator = currentRow.keySet().iterator();
			while (rowDataIterator.hasNext()) {
				Integer colNumber = rowDataIterator.next();
				registro = currentRow.get(colNumber);
				// TODO: Format according cell type
				if (registro instanceof  Number) {
					// TODO: Format numeric values
					table.addCell(createLabelCell(registro.toString()));
					log.debug("Number: " + registro);
				} else if (registro instanceof Date) {
					// TODO: Format date values
					table.addCell(createLabelCell(registro.toString()));
					log.debug("Date: " + registro);
				} else if (registro instanceof Boolean) {
					// TODO: Format boolean values
					table.addCell(createLabelCell(registro.toString()));
					log.debug("Boolean: " + registro);
				} else {
					table.addCell(createLabelCell(registro.toString()));
					log.debug("String: " + registro);
				}
			}// End column iteration
		}// End row iteration

		return table;
	}

	// create cells
	private static PdfPCell createLabelCell(String text) {
		// font
		Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD,
				BaseColor.DARK_GRAY);

		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, font));

		// set style
		Style.labelCellStyle(cell);
		return cell;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
