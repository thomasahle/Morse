package org.rosenspejd.morse.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.core.client.GWT;

public class KalliopeSource implements TextSource {
	
	private final static String INDEX_URL = "http://www.kalliope.org/klines.pl?mode=1&forbogstav=";
	private final static String TEXT_URL = "http://www.kalliope.org/digt.pl?longdid=";
	private Map<Character,String[]> pageCache;
	
	public KalliopeSource() {
		pageCache = new HashMap<Character,String[]>();
	}
	
	@Override
	public String createRequestURL() {
		int firstLetter = (int)(Math.random()*29);
		char letter = 'A';
		if (firstLetter < 26)
			letter = (char)((int)'A' + firstLetter);
		else if (firstLetter == 26)
			letter = 'Æ';
		else if (firstLetter == 27)
			letter = 'Ø';
		else if (firstLetter == 28)
			letter = 'Å';
		
		String[] urls = pageCache.get(letter);
		if (urls == null)
			return INDEX_URL + letter;
		
		int url = (int)(Math.random()*urls.length);
		return TEXT_URL + urls[url];
	}
	
	private final static String indexMark = "<p class=\"digtliste\">";
	@Override
	public void parseHtmlData(String html, Queue<String> buffer) {
		if (html.contains(indexMark))
			parseIndexPage(html);
		else
			parseTextPage(html, buffer);
	}
	
	private final static String indexStart = "digt.pl?longdid=";
	private final static String indexEnd = "\">";
	private void parseIndexPage(String html) {
		int start = html.indexOf(indexStart) + indexStart.length();
		int end = html.indexOf(indexEnd, start);
		
		if (start < 0 || end < 0 || end <= start) {
			GWT.log("Kalliope; No urls on indexpage");
			return;
		}
		
		Character page = Character.toUpperCase(html.charAt(end + indexEnd.length()));
		ArrayList<String> urls = new ArrayList<String>();
		
		do {
			urls.add(html.substring(start, end));
			start = html.indexOf(indexStart, end) + indexStart.length();
			end = html.indexOf(indexEnd, start);
		} while (start > 0);
		
		pageCache.put(page, urls.toArray(new String[urls.size()]));
		GWT.log("Kalliope; Added " + urls.size() + " urls to letter " + page);
	}
	
	private final static String textStart = "left\" nowrap>";
	private final static String endMark = "</td>";
	private void parseTextPage(String html, Queue<String> buffer) {
		int start = html.indexOf(textStart) + textStart.length();
		int end = html.indexOf(endMark, start);
		
		if (start < 0 || end < 0 || end <= start) {
			GWT.log("Kalliope; No lines in expected area in html");
			return;
		}
		
		do {
			String line = html.substring(start, end);
			line = line.replace("&nbsp;", " ").trim();
			if (!line.isEmpty()) {
				GWT.log("Kalliope; Added line: " + line);
				buffer.add(line);
			}
			
			start = html.indexOf(textStart, start) + textStart.length();
			end = html.indexOf(endMark, start);
		} while (start > 0);
	}
}
