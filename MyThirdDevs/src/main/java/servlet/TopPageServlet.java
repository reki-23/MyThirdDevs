package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/TopPageServlet")
public class TopPageServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
//			//一覧取得
//			List<TodoInfo> todoList = EditDataDao.getRegisteredTask();
//			
//			//取得結果をJSPにフォワード
//			request.setAttribute("todoList", todoList);
//			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
//			dispatcher.forward(request, response);
			pagingHandleOfAllTask(request, response);
			
		}catch(Exception e) {
			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
