package Test;

import java.util.regex.Pattern;

public class Regex {
public static void main(String[] args) {
	Pattern  javascriptRefererPattern = Pattern.compile(".*");
	System.out.println(javascriptRefererPattern.matcher("jjjjjjjj").matches());
	
}
}
