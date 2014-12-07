package it.demo.twitterlike.rest.api.util;

public class UrlUtils {

	private UrlUtils() {
		
	}
	
	public static String appendUrls(String... urls) {

		StringBuilder result = new StringBuilder();

		for (String current : urls) {
			if (current != null) {
				if (!current.startsWith("http") && !current.startsWith("/")) {
					result.append("/");
				}
				result.append(current);
				if (current.endsWith("/")) {
				result.deleteCharAt(result.length()-1);
				}	
			}	
		}
		return result.toString();
		
	}
}
