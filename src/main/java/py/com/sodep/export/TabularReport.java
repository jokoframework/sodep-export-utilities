package py.com.sodep.export;

import java.util.List;

import py.com.sodep.export.exception.ConvertException;

import com.google.common.collect.Table;

public interface TabularReport {

	public enum TYPES {
		CSV, EXCEL, PDF
	};

	public void generateReportFile(Table<Integer, Integer, Object> data,
			List<String> header, String testFileName) throws ConvertException;

}
