package common;

import java.util.HashMap;
import java.util.Map;

/*
 * 共通のエラーメッセージを管理するクラス
 * プロパティファイルに本来はメッセージを管理するが、プロパティファイル自体が読み込めない場合や読み込む前に異常が発生した場合のメッセージ  
 */

public class CommonMessage {

	private static Map<String, String> commonMessages = new HashMap<String, String>();
	
	private static final String unexpectedError = "ID000:想定外のエラーが発生しました。管理者に連絡してください。";
	

	public CommonMessage() {
		
		/*
		 * プロパティファイル読み込み失敗時のエラーメッセージ
		 */
		commonMessages.put("EM001", "ID001:エラーが発生しました。管理者に連絡して下さい。");
		
		/*
		 * IOException時のメッセージ
		 */
		commonMessages.put("EM002", "ID002:エラーが発生しました。管理者に連絡して下さい。");
		
		/*
		 * SQLException時のメッセージ
		 */
		commonMessages.put("EM003", "ID003:エラーが発生しました。管理者に連絡して下さい。");
		
		/*
		 * InvalidExistsException時のメッセージ
		 */
		commonMessages.put("EM004", "ID004:データの形式が有効ではありません。");
		commonMessages.put("EM006", "ID006:有効ではないidが含まれています。");
		commonMessages.put("EM007", "ID007:有効ではないステータスが含まれています。");
		
		/*
		 * NumberFormatException時のメッセージ
		 */
		commonMessages.put("EM008", "ID008:idの形式が有効ではありません。");
		
		/*
		 * SQLIntegrityConstraintViolationException時のメッセージ
		 */
		commonMessages.put("EM009", "ID009:既に登録されているタスクは登録できません。");
	}
	
	
	/*
	 * IDに対応したメッセージを返す
	 */
	public String getCommonMessage(String messageId) {
		
		if(!commonMessages.containsKey(messageId)) {
			return unexpectedError;
		}else {
			return commonMessages.get(messageId);
		}
		
	}
}
