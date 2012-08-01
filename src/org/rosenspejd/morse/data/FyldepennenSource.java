package org.rosenspejd.morse.data;

import java.util.Queue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

public class FyldepennenSource implements TextSource {
	@Override
	public String createRequestURL() {
		return "http://www.fyldepennen.dk/tekster/roulette/5";
	}
	
	private final static String startMark0 = "class=digte";
	private final static String startMark1 = "; \">";
	private final static String endMark = "</p></td>";
	@Override
	public void parseHtmlData(String html, Queue<String> buffer) {
		int start = html.indexOf(startMark0) + startMark0.length();
		start = html.indexOf(startMark1, start) + startMark1.length();
		int end = html.indexOf(endMark, start);
		
		if (start < 0 || end < 0 || end <= start) {
			GWT.log("Fyldepennen; No lines in expected area in html");
			return;
		}
		
		String lines = html.substring(start, end);
		GWT.log("Fyldepennen; Unpacked " + lines.length() + " chars of lines");
		for (String line : lines.split("<br />")) {
			line = line.trim();
			if (!line.isEmpty()) {
				GWT.log("Fyldepennen; Added line: " + line);
				buffer.add(line);
			}
		}
	}
}
