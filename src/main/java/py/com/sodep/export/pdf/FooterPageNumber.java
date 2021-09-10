package py.com.sodep.export.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class FooterPageNumber extends PdfPageEventHelper{
	@Override
	 public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
	    ColumnText.showTextAligned(writer.getDirectContent(),
             Element.ALIGN_CENTER, new Phrase(String.format("Página %d", writer.getPageNumber())),
             (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
	 }

}
