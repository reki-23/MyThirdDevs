package model.exception;

import java.io.IOException;

public class PropertiesFileNotFoundException extends IOException{
	
	private static final long serialVersionUID = 1L;
	
	public PropertiesFileNotFoundException() {
		super();
	}
	
	public PropertiesFileNotFoundException(String message) {
		super(message);
	}
}
