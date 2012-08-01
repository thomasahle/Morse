package org.rosenspejd.morse.data;

import java.util.Queue;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

public interface TextSource {
	public String createRequestURL();
	public void parseHtmlData(String html, Queue<String> buffer);
}
