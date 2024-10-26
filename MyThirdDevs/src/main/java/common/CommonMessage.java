package common;

import java.util.HashMap;
import java.util.Map;

/*
 * 共通のエラーメッセージを管理するクラス
 * プロパティファイルに本来はメッセージを管理するが、プロパティファイル自体が読み込めない場合や読み込む前に異常が発生した場合のメッセージ  
 */

public class CommonMessage {

	private static Map<String, String> commonMessages = new HashMap<String, String>();
	

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
		commonMessages.put("EM003", "ID003:データベースに関するエラーが発生しました。管理者に連絡して下さい。");
	}
	
	
	/*
	 * IDに対応したメッセージを返す
	 */
	public String getCommonMessage(String messageId) {
		
		if(!commonMessages.containsKey(messageId)) {
			return "想定外のエラーです。管理者に連絡して下さい。";
		}else {
			return commonMessages.get(messageId);
		}
		
	}
}
