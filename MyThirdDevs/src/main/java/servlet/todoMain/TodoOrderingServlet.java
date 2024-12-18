package servlet.todoMain;

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
	private static int any_pushedCounta = 0;
	
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
			
			//並べかえの処理を行わない場合
			if(tHeaderParameter == null || tHeaderParameter.isBlank()) {
				pagingHandleOfAllTask(request, response);
				return;
			}
			
			//降順か昇順かを決定する並べかえ用のカウンタ
			String tmpany_pushedCounta = request.getParameter("any_pushedCounta");
			if(tmpany_pushedCounta != null) {
				any_pushedCounta = Integer.valueOf(tmpany_pushedCounta);
			}

			request.setAttribute("any_pushedCounta", any_pushedCounta);
			request.setAttribute("tHeaderParameter", tHeaderParameter);
			
			//ページごとに表示するタスクを格納したリスト
			//TODO 2つ目の引数である数字をどう表現するか
			List<TodoInfo> orderedTaskList = EditDataDao.getListOrderByParameters(tHeaderParameter, any_pushedCounta);
			TodoSearchServlet.pagingHandleOnlyMatchTask(request, response, orderedTaskList);
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
