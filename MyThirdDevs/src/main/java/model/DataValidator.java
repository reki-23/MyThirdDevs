package model;

import exception.InvalidDataExistsException;
import exception.ManageException;

/*
 * 一括登録時に渡されるファイルの各データを検査する
 */

class DataValidator{
	
	protected static String getFieldsSafely(String fields[], int index) {
		if(fields[index].length() == 0) {
			
		}
		return null;
	}
	
	protected static int returnValidId(String[] fields) throws ManageException{
		
		try {
			int id = Integer.parseInt(fields[0]);
			return id;
		}catch(NumberFormatException e) {
			throw new ManageException("", e);
		}
	}
	
	protected static String returnValidStatus(String[] fields) throws ManageException{
		
		boolean isValidStatus = fields[1].equals("未着手") || fields[1].equals("対応中") || fields[1].equals("完了") || fields[1].equals("保留") || fields[1].equals("取下げ");
		if(!isValidStatus) {
			throw new ManageException("", new InvalidDataExistsException());
		}
		return fields[1];
	}
	
}
