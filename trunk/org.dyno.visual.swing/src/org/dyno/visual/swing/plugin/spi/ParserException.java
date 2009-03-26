package org.dyno.visual.swing.plugin.spi;

import java.io.BufferedReader;
import java.io.StringReader;

public class ParserException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public ParserException(String message) {
		setMessage(message);
	}

	public void setMessage(String msg) {
		try {
			StringReader sr = new StringReader(msg);
			BufferedReader br = new BufferedReader(sr);
			message = "<html><body>";
			String line;
			while ((line = br.readLine()) != null) {
				message += "<p>" + line + "</p>";
			}
			br.close();
			message += "</body></html>";
		} catch (Exception ex) {
		}
	}

	public ParserException(Throwable e) {
		Throwable t = e;
		while (t != null) {
			e = t;
			t = t.getCause();
		}
		setMessage(e.getMessage());
	}

	@Override
	public String getMessage() {
		return message;
	}
}
