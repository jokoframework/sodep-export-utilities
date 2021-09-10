package py.com.sodep.export.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

@Deprecated
/**
 * The methods of this class are too specific for Captura. Some of them already
 * exist in MFDataHelper, and the the formats are copied and exposed again here.
 * 
 * The methods formatDateForTextOnlyReport and formatDateTimeForTextOnlyReport make wrong assumptions
 * when setting the timezone to "GMT" a using short Date ("dd/MM/yyyy") and Time ("HH:mm") formats.
 *
 * There's also a performance issue due to the creation of an instance of SimpleDateFormat in 
 * every call to the above methods.
 *
 */
public class TextFormatUtil {

	/* Default formats */
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SS'Z'";
	private static final String TIME_FORMAT = "HH:mm";
	private static final String TIMEZONE = "GMT";
	private static final String AMOUNT_FORMAT = "#,###.00";

	/**
	 * Formats amount with thousands separator according to current Locale
	 * 
	 * @param object
	 * @return
	 */
	public static String formatAmount(Object object, String format) {
		String ret = null;
		if (object != null) {
			if (StringUtils.isBlank(format))
				format = AMOUNT_FORMAT;
			//FIXME where is format used?
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			BigDecimal valor = new BigDecimal("0");
			if (object instanceof BigDecimal) {
				valor = (BigDecimal) object;
			} else {
				valor = new BigDecimal(object.toString());
			}
			ret = numberFormat.format(valor);
		}
		return ret;
	}

	public static String formatAmount(Object object) {
		return formatAmount(object, AMOUNT_FORMAT);
	}

	public static String formatDateForTextOnlyReport(Date d) {
		return formatDateForTextOnlyReport(d, DATE_FORMAT);
	}

	public static String formatDateForTextOnlyReport(Date d, String format) {
		if (d == null)
			return null;
		if (StringUtils.isBlank(format))
			format = DATE_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(d);
	}

	public static String formatDateTimeForTextOnlyReport(Date d) {
		return formatDateForTextOnlyReport(d, DATE_TIME_FORMAT);
	}

	public static String formatDateTimeForTextOnlyReport(Date d, String format) {
		if (d == null)
			return null;
		if (StringUtils.isBlank(format))
			format = DATE_TIME_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(d);
	}

	public static String timeForTextOnlyReport(Date d) {
		return timeForTextOnlyReport(d, "");
	}

	public static String timeForTextOnlyReport(Date d, String format) {
		if (d == null)
			return null;
		if (StringUtils.isBlank(format))
			format = TIME_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		return sdf.format(d);
	}

}
