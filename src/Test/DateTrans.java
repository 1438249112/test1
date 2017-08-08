package Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTrans {
	public static void main(String ...arg) throws ParseException {
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(new Locale("en", "US"));
		String timeStr = "";
		try {
			DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	    	
	    	DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	java.util.Date time = null;
			String date_Str = "10/3/2016 9:04:45 AM";
			time = (Date) format1.parse(date_Str);
			
			timeStr = format2.format(time);
	    	
		} catch (Exception e) {
			timeStr = "";
		}
	
		Locale.setDefault(defaultLocale);
		System.out.println(timeStr);
		//return timeStr;
    }
}
