package SFDC2IDP.MAIN;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TestMain {
public static void main(String[] args) throws UnsupportedEncodingException {
	String date = "2016-12-12T10:32:56.000+0000";
	//2016-12-12T18:32:56.001+08:00
	//2016-12-12T14:03:47.001+08:00
//	Object o = new java.math.BigDecimal(Math.round(new java.math.BigDecimal(intNumber).doubleValue())).toPlainString();
//System.out.println(o);
	//	String date = "2012-11-17";
DateTimeZone	zone = org.joda.time.format.ISODateTimeFormat.dateTime().parseDateTime(date).getZone();
DateTime dateTime	= org.joda.time.format.ISODateTimeFormat.dateTime().parseDateTime(date);
DateTime newDateTime = dateTime.plusMillis(1);
System.out.println(dateTime.toDateTime(zone));
//	System.out.println(	org.apache.commons.lang.time.DateFormatUtils.formatUTC(org.joda.time.format.ISODateTimeFormat.dateTime().parseDateTime(date).getMillis(), "yyyy-MM-dd HH:mm:ss:SSS", java.util.Locale.CHINA).toString()
//);
//System.out.println(new java.math.BigDecimal(Math.round(new java.math.BigDecimal(intNumber).doubleValue())).toPlainString());


String test = "将截断字符串或二进制数据�?";
System.out.println(new String (test.getBytes(),"UTF8"));
}
public static void main2(String[] args) {
	
	String  date = new  java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new java.util.Date()).toString()+"";
    System.out.println(date);
	  date = org.apache.commons.lang.time.DateFormatUtils.formatUTC(new java.util.Date().getTime(), "yyyy-MM-dd HH:mm:ss:SSS",java.util.Locale.CHINESE).toString()+"";
     System.out.println(date);
}
}
