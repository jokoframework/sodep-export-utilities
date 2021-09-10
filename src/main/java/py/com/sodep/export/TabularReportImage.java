package py.com.sodep.export;

import java.io.FileNotFoundException;
import java.util.List;

import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.Table;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

public interface TabularReportImage {
	public enum TYPES {
		CSV, EXCEL, PDF
	};

	public void generateReportFileImage(Table<Integer, Integer, Object> data,
			List<String> header, String testFileName, Image imagen) throws ConvertException, DocumentException, FileNotFoundException;

}
