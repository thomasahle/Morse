package org.rosenspejd.morse.data;

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class WebGenerator implements TextGenerator {
	private TextSource source;
	private Queue<String> buffer;
	private Queue<TextCallback> waiting;
	
	public WebGenerator(TextSource source) {
		this.source = source;
		buffer = new LinkedList<String>();
		waiting = new LinkedList<TextCallback>();
	}
	
	private void requestMore() {
		String url = source.createRequestURL();
		GWT.log("WebGenerator; Requesting more lines from " + url);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setCallback(callback);
		//builder.setTimeoutMillis(int timeoutMillis);
		try {
			builder.send();
		} catch (RequestException e) {
			// This should never happen with the given type of request.
			GWT.log("WebGenerator; Generating request failed", e);
		}
	}
	
	@Override
	public void nextText(TextCallback callback) {
		if (!buffer.isEmpty()) {
			GWT.log("WebGenerator; Gave text from buffer");
			callback.onTextReady(buffer.remove());
		} else {
			waiting.add(callback);
			requestMore();
		}
	}
	
	private RequestCallback callback = new RequestCallback() {
		@Override
		public void onResponseReceived(Request request, Response response) {
			GWT.log("WebGenerator; Got " + response.getText().length() + " chars of html");
			source.parseHtmlData(response.getText(), buffer);
			while (!waiting.isEmpty() && !buffer.isEmpty())
				waiting.remove().onTextReady(buffer.remove());
			if (buffer.isEmpty())
				requestMore();
		}
		
		@Override
		public void onError(Request request, Throwable exception) {
			for (TextCallback waiter : waiting)
				waiter.onError(exception);
			waiting.clear();
		}
	};
}
