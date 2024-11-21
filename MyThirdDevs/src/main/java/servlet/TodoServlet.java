package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonMessage;
import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;
import exception.PropertiesFileNotFoundException;

/**
 * 
 * @see全てのサーブレットクラスのスーパークラス
 * @see共通処理のエラーハンドリング処理とタスク一覧取得処理をもつ
 * 
 */

public class TodoServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	
	//プロパティファイルを読み込む
	protected void getPropertiesFileInfo(HttpServletRequest request, HttpServletResponse response, Properties prop) throws ServletException, IOException{
		try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
			if(input == null) {
				//読み込み失敗時
				throw new ManageException("EM001", new PropertiesFileNotFoundException());
			}
			
			try(InputStreamReader reader = new InputStreamReader(input, "UTF-8")){
				prop.load(reader);
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	//エラーハンドリング処理
	//ManageExceptionがcatchされた場合の処理
	protected static void errorHandle(HttpServletRequest request, HttpServletResponse response, ManageException e) throws ServletException, IOException {
		CommonMessage commonMessage = new CommonMessage();
		String errorMessage = commonMessage.getCommonMessage(e.getMessageId());
		request.setAttribute("errorMessage", errorMessage);
		displayRegisteredTask(request, response);
	}
	
	
	//タスク一覧取得
	protected static void displayRegisteredTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			List<TodoInfo> todoList = EditDataDao.getRegisteredTask();
			request.setAttribute("todoList", todoList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);			
		}catch(ManageException e) {
			CommonMessage commonMessage = new CommonMessage();
			String errorMessage = commonMessage.getCommonMessage(e.getMessageId());
			request.setAttribute("errorMessage", errorMessage);
		}
	}
}
