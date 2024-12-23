package model.exception;

import java.io.IOException;

public class NoElementsOnFileException extends IOException{
	
	private static final long serialVersionUID = 1L;
	
	public NoElementsOnFileException() {
		super();
	}
	
	public NoElementsOnFileException(String message) {
		super(message);
	}
}
