package MDS2ODH.MAIN;

public class test {
public static void main(String[] args) {
	String result = "";
	String values = "(0, '', '', '', '', '', '', '', 0, 0, '', '', '', '', '', '', '', '', '', '', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '', '', '', '', '', '', '', '', '', 0, 0, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 0, '', '', '', '', '', 0, '', '', '', '2016-11-23 14:54:52', '2016-11-23 14:54:52', '', '2016-11-23 14:54:52', '')";
	for (int i = 0; i < 1000; i++) {
		result+=values+",";
	}
	System.out.println(result);
}
}
