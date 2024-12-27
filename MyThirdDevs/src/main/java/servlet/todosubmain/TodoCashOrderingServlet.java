package servlet.todosubmain;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.common.TodoInfo;
import model.dao.EditDataDao;
import model.exception.ManageException;
import servlet.TodoServlet;

/**
 * Servlet implementation class TodoCashServlet
 */
@WebServlet("/TodoCashOrderingServlet")
public class TodoCashOrderingServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
	private static int any_pushedCounta = 0;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
		forwardTodoCashList(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
	}
	
	
	
	//ゴミ箱内のデータを
	
	
	//ゴミ箱内のデータを並べかえするメソッド
	protected void getOrderedHandleTask(HttpServletRequest request, HttpServletResponse response, String tHeaderParameter) throws ServletException, IOException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//並べかえの処理を行わない場合
			if(tHeaderParameter == null || tHeaderParameter.isBlank()) {
				request.setAttribute("tHeaderParameter", tHeaderParameter);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/TodoCashSearchServlet");
				dispatcher.forward(request, response);
				return;
			}
			
			//降順か昇順かを決定する並べかえ用のカウンタ
			String tmpany_pushedCounta = request.getParameter("any_pushedCounta");
			if(tmpany_pushedCounta != null) {
				any_pushedCounta = Integer.valueOf(tmpany_pushedCounta);
			}
			
			//ページごとに表示するタスクを格納したリスト
			//TODO 2つ目の引数である数字をどう表現するか
			List<TodoInfo> orderedTaskList = EditDataDao.getCashListOrderByParameters(tHeaderParameter, any_pushedCounta);
			
			request.setAttribute("any_pushedCounta", any_pushedCounta);
			request.setAttribute("tHeaderParameter", tHeaderParameter);
			pagingHandleOfSpecificCashTask(request, response, orderedTaskList);
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
}
