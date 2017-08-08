package Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * [‎2017/‎2/‎28 15:39] Yulong YL3 Bai: 
1488240000
1488326400
1488086400
今天0点的时间戳
[‎2017/‎2/‎28 15:40] Yulong YL37 Zhang: 
对了 你那是咋生成的？
没准我能copy呢  呵呵
[‎2017/‎2/‎28 15:42] Yulong YL3 Bai: 
呃，我是这么取的，当前时间戳 - (当前时间戳 % 86400)
%号是取余数的意思

 * @author litsoft
 *
 */
public class TestMain {
public static void main(String[] args) {
//	long times = 1488343579970L;
//	System.out.println(times/1000-times/1000%86400);
	System.out.println("123\n345\001456");
//	Locale.setDefault(new Locale("en", "US"));
//    DateFormat format1 = new SimpleDateFormat("MM/d/yyyy KK:mm:ss a");
//  
//	DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Date time = null;
//	try {
//		time = format1.parse("10/3/2016 9:20:04 Am");
//		String timeStr = format2.format(time);
//		System.out.println(timeStr);
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//	System.out.println( format1.format(new Date()));
}
}
