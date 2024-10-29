package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;

/**
 * Servlet implementation class TodoSearchServlet
 */
@WebServlet("/TodoSearchServlet")
public class TodoSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			//取得した一覧のデータを格納するリストと一覧取得
			List<TodoInfo> todoList = EditDataDao.getRegisteredTask();
			
			request.setAttribute("todoList", todoList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);
		}catch(Exception e) {
			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
	}
}
