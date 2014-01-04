package pl.eiti.bpelag.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringElemUtil {
	public static Boolean isRelative(String path) {
		return !path.matches("(.*[:])(\\\\|\\/)(.*)([.]wsdl)");
	}

	public static String getPath(String bpelPath) {
		Pattern pattern = Pattern.compile(".*[\\\\|\\/]");
		Matcher matcher = pattern.matcher(bpelPath);
		String result = "";
		if(matcher.find()) {
			result = matcher.group();
		}
		return result;
	}
}
