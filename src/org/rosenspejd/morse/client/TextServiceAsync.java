package org.rosenspejd.morse.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The async counterpart of <code>TextService</code>.
 */
public interface TextServiceAsync {
	void nextText(AsyncCallback<String> callback);
}
