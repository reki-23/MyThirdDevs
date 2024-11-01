package exception;

import java.io.IOException;

public class InvalidDataExistsException extends IOException{
	
	private static final long serialVersionUID = 1L;
	
	public InvalidDataExistsException() {
		super();
	}
	
	public InvalidDataExistsException(String message) {
		super(message);
	}
}
