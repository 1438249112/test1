package Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CopyOfDateTrans {
	public static void main(String ...arg) throws ParseException {
		
		String  date = "12345678";
		System.out.println(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
	}
}

