package model.exception;

import java.io.IOException;

public class InappropriateFileException extends IOException{
	
	private static final long serialVersionUID = 1L;
	
	public InappropriateFileException() {
		super();
	}
	
	public InappropriateFileException(String message) {
		super(message);
	}
}
