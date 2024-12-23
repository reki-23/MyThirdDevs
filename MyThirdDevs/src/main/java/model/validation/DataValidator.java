package model.validation;

import java.time.LocalDateTime;

import model.exception.InvalidDataExistsException;
import model.exception.ManageException;

/*
 * 一括登録時に渡されるファイルの各データを検査する
 */

public class DataValidator{
	
	//データが欠損している場合例外を投げる
	public static String getFieldsSafely(String fields[], int index) throws ManageException{
		if(fields[index].length() == 0) {
			throw new ManageException("EM004", new InvalidDataExistsException());
		}else {
			return fields[index];
		}
	}
	
	//タスクNo.が自然数かどうか判定
	public static int returnValidId(String field) throws ManageException{
		try {
			//そもそもidがnullやブランクの場合、-1を固定で返す
			if(field == null || field.isBlank()) {
				return -1;
			}
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
	public static String returnValidStatus(String field) throws ManageException{
		boolean isValidStatus = field.equals("未着手") || field.equals("対応中") || field.equals("完了") || field.equals("保留") || field.equals("取下げ");
		if(!isValidStatus) {
			throw new ManageException("EM007", new InvalidDataExistsException());
		}
		return field;
	}
	
	//日付けのフォーマットチェック＝登録時は使用しないが、フィルターをかけてエクスポートする際に使用する
	public static LocalDateTime returnValidDateTime(String dateTime) throws ManageException{
		if(dateTime == null || dateTime.isBlank()) {
			//dateTimeがnullの場合、適当に遠い過去の時間を設定しておく
			return LocalDateTime.MIN;
		}else {
			return LocalDateTime.parse(dateTime);
		}
	}
	
	//お気に入りの型チェック
	public static boolean returnValidFavoriteFlg(String tmpIsFavorite) throws ManageException{
		if(tmpIsFavorite != null) {
			return true;
		}
		return false;
	}
	
}
