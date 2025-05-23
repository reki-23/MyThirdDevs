package model.exception;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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
	 * NumberFormatExceptionの情報を管理するクラス
	 */
	public ManageException(String errorMessageId, NumberFormatException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * InvalidDataExistsExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, InvalidDataExistsException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * RuntimeExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, RuntimeException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * SQLIntegrityConstraintViolationExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, SQLIntegrityConstraintViolationException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * NoElementsOnFileExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, NoElementsOnFileException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * FileOverSizeExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, FileOverSizeException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * InappropriateFileExceptionの情報を管理する
	 */
	public ManageException(String errorMessageId, InappropriateFileException e) {
		this.errorMessageId = errorMessageId;
	}
	
	/*
	 * メッセージIDを返す
	 */
	public String getMessageId(){
		return errorMessageId;
		
	}
}
