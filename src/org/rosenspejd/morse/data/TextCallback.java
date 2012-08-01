package org.rosenspejd.morse.data;

public interface TextCallback {
	public void onTextReady(String text);
	public void onError(Throwable exception);
}
