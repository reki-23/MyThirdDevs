package servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonMessage;
import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;

/**
 * タスク削除用サーブレット
 */
@WebServlet("/TodoDeleteServlet")
public class TodoDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		try {

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//一括削除時の値
			String bulkDel = request.getParameter("bulkDel");
			//選択されたタスクidを取得=個別削除時
			String selectedIds = request.getParameter("selectedIds");
			
			//一括削除処理
			if(bulkDel != null) {
				boolean bulkDeleteJudge = EditDataDao.bulkDeleteTask();
				request.setAttribute("deleteJudge", bulkDeleteJudge);
				displayRegisteredTask(request, response);
			}
			
			//個別削除処理
			if(selectedIds != null && !selectedIds.isEmpty()){
				String[] selectedId = selectedIds.split(",");
				List<Integer> deletedIdList = Arrays
						.stream(selectedId)
						.map(Integer::valueOf)
						.collect(Collectors.toList());
				boolean individualDeleteJudge = EditDataDao.individualDeleteTask(deletedIdList);
				request.setAttribute("deleteJudge", individualDeleteJudge);
				displayRegisteredTask(request, response);
			}
			
		}catch(ManageException e) {
			errorHandle(request, response, e);		
		}
	}
	
	//タスク一覧取得
	private static void displayRegisteredTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
		
		//エラー専用ページに遷移させ、メッセージ表示
	}
	
	//ManageExceptionがcatchされた場合の処理
	private static void errorHandle(HttpServletRequest request, HttpServletResponse response, ManageException e) throws ServletException, IOException {
		CommonMessage commonMessage = new CommonMessage();
		String errorMessage = commonMessage.getCommonMessage(e.getMessageId());
		request.setAttribute("errorMessage", errorMessage);
		displayRegisteredTask(request, response);
	}
}
