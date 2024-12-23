package model.exception;

import java.io.IOException;

public class FileOverSizeException extends IOException{
	
	private static final long serialVersionUID = 1L;
	
	public FileOverSizeException() {
		super();
	}
	
	public FileOverSizeException(String message) {
		super(message);
	}
}
