package org.rosenspejd.morse.server;

import org.rosenspejd.morse.client.TextService;
import org.rosenspejd.morse.data.KalliopeSource;
import org.rosenspejd.morse.data.TextCallback;
import org.rosenspejd.morse.data.TextGenerator;
import org.rosenspejd.morse.data.WebGenerator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TextServiceImpl extends RemoteServiceServlet implements
		TextService {
	
	final TextGenerator gen = new WebGenerator(new KalliopeSource());
	
	public String nextText() {
		TextCallback callback = new TextCallback() {
			@Override
			public void onTextReady(String text) {
				setTextToReturn(text);
			}

			@Override
			public void onError(Throwable exception) {
				throw new RuntimeException(exception);
			}
		};
		gen.nextText(callback);
		while (next == null) {
			try {
				callback.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		String text = next;
		next = null;
		return text;
	}
	
	String next = null;
	private void setTextToReturn(String text) {
		next = text;
	}
}
