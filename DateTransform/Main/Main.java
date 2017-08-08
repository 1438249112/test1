package Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import SFDC2IDP.BASE.COMMON.Helper;

public class Main {
	public static void main(String[] args) {
		String date = "2017-04-29T04:35:29.000+08:00";
//		for (int i = 0; i < 1000; i++) {
	
		    DateTime datetime = org.joda.time.format.ISODateTimeFormat.dateTime().parseDateTime(date.toString()).minusHours(8).plusMinutes(3);
		
//			if(result.contains("70")){
//				System.out.println(i);
//			}
//		}
		      System.out.println(datetime.toDateTimeISO());
			   boolean  isBeforeNow = datetime.isBeforeNow();
			   System.out.println(isBeforeNow);
			   System.out.println(new DateTime().toDateTimeISO());


	}
}
