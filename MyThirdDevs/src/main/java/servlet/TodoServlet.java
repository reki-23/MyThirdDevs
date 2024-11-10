package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonMessage;
import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;

/**
 * 
 * @see全てのサーブレットクラスのスーパークラス
 * @see共通処理のエラーハンドリング処理とタスク一覧取得処理をもつ
 * 
 */

public class TodoServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
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
