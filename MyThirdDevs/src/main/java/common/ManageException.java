package common;

import java.io.IOException;
import java.sql.SQLException;

/*
 *
 * 発生しうる例外を管理するクラス
 * 
 */

public class ManageException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * 各例外のメッセージID
	 */
	private String errorMessageId;
	
	
	/*
	 * PropertiesFileNotFoundExceptionの情報を管理するクラス
	 */
	public ManageException(String errorMessageId, PropertiesFileNotFoundException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * IOExceptionの情報を管理するクラス
	 */
	public ManageException(String errorMessageId, IOException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * SQLExceptionの情報を管理するクラス
	 */
	public ManageException(String errorMessageId, SQLException e) {
		this.errorMessageId = errorMessageId;
	}
	
	
	/*
	 * メッセージIDを返す
	 */
	public String getMessageId(){
		return errorMessageId;
		
	}
}
