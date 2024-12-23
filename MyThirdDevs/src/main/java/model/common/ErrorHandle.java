package model.common;

import model.exception.ManageException;

public class ErrorHandle {
	//エラーハンドリング処理
	//ManageExceptionがcatchされた場合の処理
	public String errorHandle(ManageException e){
		CommonMessage commonMessage = new CommonMessage();
		String errorMessage = commonMessage.getCommonMessage(e.getMessageId());
		return errorMessage;
	}
}
