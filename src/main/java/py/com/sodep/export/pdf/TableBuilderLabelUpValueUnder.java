package py.com.sodep.export.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import py.com.sodep.export.TabularReportImage;
import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.Table;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class TableBuilderLabelUpValueUnder implements TabularReportImage {
	static Logger log = Logger.getLogger(TableBuilder.class);
	
	static final int MAX_COLUMN=1; 
	private String title;

	public TableBuilderLabelUpValueUnder(){
		
	}
	
	public TableBuilderLabelUpValueUnder(String title) {
		setTitle(title);
	}

	// create cells
	private static PdfPCell createLabelCell(String text) {
		// font
		Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.DARK_GRAY);

		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, font));

		// set style
		Style.labelCellStyle(cell);
		return cell;
	}
	
	// create cells
    private static PdfPCell createValueCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        Style.valueCellStyle(cell);
        return cell;
    }
    
    private static PdfPCell createImageCell(Image imagen){
    	
    	PdfPCell cell = new PdfPCell (imagen);
    	Style.imageCellStyle(cell);
    	
    	return cell;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void generateReportFileImage(Table<Integer, Integer, Object> data,
			List<String> header, String testFileName, Image imagen) throws ConvertException, DocumentException, FileNotFoundException {
		
		Document document = new Document();
		document.setPageSize(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(testFileName));
		
		Rectangle rct = new Rectangle(36, 54, 559, 788);
		writer.setBoxSize("art", rct);
		
		FooterPageNumber footer = new FooterPageNumber();
		
		writer.setPageEvent(footer);
		
		document.open();

		int columnCount = MAX_COLUMN;
		PdfPTable table = new PdfPTable(columnCount);
		
		// set the width of the table to 100% of page
		table.setWidthPercentage(100);
		
		PdfPCell cell=null;
		float[] columnWidth = new float[columnCount];
		for (int i = 0; i < columnCount; i++)
			columnWidth[i] = (float) (1f / columnCount);
		// set relative columns width
		table.setWidths(columnWidth);

		table.setHeaderRows(1);

		if (StringUtils.isNotBlank(getTitle())) {
			// ----------------Table Header "Title"----------------
			// font
			Font font = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
			// create header cell
			cell = new PdfPCell(new Phrase(getTitle(), font));
				
			cell.setColspan(columnCount);
				
			// set style
			Style.headerCellStyle(cell);
				
			// add to table
			table.addCell(cell);
		}

		// -----------------Table Cells Label/Value------------------
		try {
				
			Object registro = null;

			Iterator<Integer> rows = data.rowKeySet().iterator();

			while (rows.hasNext()) {
				Integer rowNumber = rows.next();
				int i=0;

				Map<Integer, Object> currentRow = data.row(rowNumber);
				Iterator<Integer> rowDataIterator = currentRow.keySet().iterator();
				while (rowDataIterator.hasNext()) {
					table.addCell(createLabelCell(header.get(i)));
					
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

					//Create a header row
					Integer colNumber = rowDataIterator.next();
					registro = currentRow.get(colNumber);
				    
					// TODO: Format according cell type
					if (NumberUtils.isNumber(registro.toString())) {
						// TODO: Format numeric values
						table.addCell(createValueCell(registro.toString()));
						log.debug("Number: " + registro);
					} else if (registro instanceof Date) {
						// TODO: Format date values
						table.addCell(createValueCell(registro.toString()));
						log.debug("Date: " + registro);
					} else if (registro instanceof Boolean) {
						// TODO: Format boolean values
						table.addCell(createValueCell(registro.toString()));
						log.debug("Boolean: " + registro);
					} else if(registro instanceof String){
						//TODO: Format string values
						cell=createValueCell(registro.toString());
						cell.setVerticalAlignment(columnCount);
						table.addCell(cell);
						log.debug("String: " + registro);
					}else{
						//TODO: Format image values
						cell=createImageCell(imagen);
						//Image scale
						imagen.scalePercent(40);
						
						table.addCell(cell);
						log.debug("Image: " + registro);
					}
					i++;

				}// End column iteration
				
				document.add(table);
				table.deleteBodyRows();
				table.setHeaderRows(0);
				table.deleteLastRow();
				
				//Insert new line
				Paragraph parrafo2 = new Paragraph("\n");
				document.add(parrafo2);
				
				//Align the image to the center
				imagen.setAlignment(Image.ALIGN_CENTER);
				//Insert new page
				document.add(Chunk.NEXTPAGE);
				
			}// End row iteration
		
		} catch (DocumentException e) {
			throw new ConvertException(e);

		}

		document.close();	      

	}
}


