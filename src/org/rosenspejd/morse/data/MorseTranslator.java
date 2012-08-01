package org.rosenspejd.morse.data;

import java.util.Map;
import java.util.HashMap;

public class MorseTranslator {
	private MorseTranslator instance = null;

	private static final String[][] morseArray = new String[][] {
			{ "a", ".-" }, { "b", "-..." }, { "c", "-.-." }, { "d", "-.." },
			{ "e", "." }, { "f", "..-." }, { "g", "--." }, { "h", "...." },
			{ "i", ".." }, { "j", ".---" }, { "k", "-.-" }, { "l", ".-.." },
			{ "m", "--" }, { "n", "-." }, { "o", "---" }, { "p", ".--." },
			{ "q", "--.-" }, { "r", ".-." }, { "s", "..." }, { "t", "-" },
			{ "u", "..-" }, { "v", "...-" }, { "w", ".--" }, { "x", "-..-" },
			{ "y", "-.--" }, { "z", "--.." }, { "æ", ".-.-" }, { "ø", "---." },
			{ "å", ".--.-" } };
	private Map<String, String> alphaToMorse;
	private Map<String, String> morseToAlpha;

	private MorseTranslator() {
		alphaToMorse = new HashMap<String, String>();
		morseToAlpha = new HashMap<String, String>();
		for (String[] alpha_morse : morseArray) {
			alphaToMorse.put(alpha_morse[0], alpha_morse[1]);
			morseToAlpha.put(alpha_morse[1], alpha_morse[0]);
		}
	}

	public MorseTranslator getInstance() {
		if (instance == null)
			instance = new MorseTranslator();
		return instance;
	}

	private StringBuffer stringBuffer = new StringBuffer();

	public String toAlpha(String morse) {
		stringBuffer.delete(0, stringBuffer.length());
		for (String letter : morse.split("/")) {
			if (letter.length() == 0)
				stringBuffer.append(' ');
			else {
				String translated = morseToAlpha.get(letter);
				assert translated != null;
				stringBuffer.append(translated);
			}
		}
		return stringBuffer.toString();
	}

	public String toMorse(String alpha) {
		stringBuffer.delete(0, stringBuffer.length());
		alpha = alpha.toLowerCase();
		for (char letter : alpha.toCharArray()) {
			if (letter == ' ')
				stringBuffer.append("//");
			else {
				String translated = alphaToMorse.get(String.valueOf(letter));
				if (translated != null) {
					stringBuffer.append(translated);
					stringBuffer.append('/');
				}
			}
		}
		while (stringBuffer.length() > 0
				&& stringBuffer.charAt(stringBuffer.length() - 1) == '/')
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		return stringBuffer.toString();
	}
}
