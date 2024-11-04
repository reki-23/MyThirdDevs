package model;

import exception.InvalidDataExistsException;
import exception.ManageException;

/*
 * 一括登録時に渡されるファイルの各データを検査する
 */

class DataValidator{
	
	//データが欠損している場合例外を投げる
	protected static String getFieldsSafely(String fields[], int index) throws ManageException{
		if(fields[index].length() == 0) {
			throw new ManageException("EM004", new InvalidDataExistsException());
		}else {
			return fields[index];
		}
	}
	
	//タスクNo.が自然数かどうか判定
	protected static int returnValidId(String field) throws ManageException{
		try {
			int id = Integer.parseInt(field);
			if(id < 0) {
				throw new InvalidDataExistsException();
			}
			return id;
		}catch(NumberFormatException e) {
			throw new ManageException("EM008", e);
		}catch(InvalidDataExistsException e) {
			throw new ManageException("EM006", e);
		}
	}
	
	//ステータスがドロップダウンの要素と等しいかどうか
	protected static String returnValidStatus(String field) throws ManageException{
		
		boolean isValidStatus = field.equals("未着手") || field.equals("対応中") || field.equals("完了") || field.equals("保留") || field.equals("取下げ");
		if(!isValidStatus) {
			throw new ManageException("EM007", new InvalidDataExistsException());
		}
		return field;
	}
	
}
