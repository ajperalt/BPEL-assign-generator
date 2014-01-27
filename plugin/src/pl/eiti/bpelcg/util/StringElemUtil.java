package pl.eiti.bpelcg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utility makes checks and operations on paths strings.
 */
public class StringElemUtil {

	/**
	 * Checks if path given as parameter is realative.
	 * 
	 * @param path
	 *            path to check.
	 * @return bool information if relative.
	 */
	public static Boolean isRelative(String path) {
		return !path.matches("(.*[:])(\\\\|\\/)(.*)([.]wsdl)");
	}

	/**
	 * Gets from path given as parameter path part without file name.
	 * 
	 * @param filePath
	 *            path to cut off file name from.
	 * @return path to folder.
	 */
	public static String getPath(String filePath) {
		Pattern pattern = Pattern.compile(".*[\\\\|\\/]");
		Matcher matcher = pattern.matcher(filePath);
		String result = "";
		if (matcher.find()) {
			result = matcher.group();
		}
		return result;
	}
}
