package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;

/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/TodoOrderingServlet")
public class TodoOrderingServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
		forwardToTodoList(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
	}
	
	protected static void getOrderedHandleTask(HttpServletRequest request, HttpServletResponse response, String tHeaderParameter) throws ServletException, IOException{
		
		try {	
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			request.setAttribute("tHeaderParameter", tHeaderParameter);
			int pushedCount = 0;
			//並べかえの処理を行わない場合
			if(tHeaderParameter == null || tHeaderParameter.isBlank()) {
				pagingHandleOfAllTask(request, response);
				return;
			}
			//ページごとに表示するタスクを格納したリスト
			//TODO 2つ目の引数である数字をどう表現するか
			pushedCount++;
			List<TodoInfo> orderedTaskList = EditDataDao.getListOrderByParameters(tHeaderParameter, pushedCount);
			TodoSearchServlet.pagingHandleOnlyMatchTask(request, response, orderedTaskList);
			pushedCount--;
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
