package common;

public class PropertiesFileNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public PropertiesFileNotFoundException() {
		super();
	}
	
	public PropertiesFileNotFoundException(String message) {
		super(message);
	}
}
