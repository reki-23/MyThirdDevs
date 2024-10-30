package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EditDataDao;

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
			
			//一括削除処理
			boolean deleteJudge = EditDataDao.bulkDeleteTask();
			request.setAttribute("deleteJudge", deleteJudge);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);
		}catch(Exception e) {
			
		}
		
	}
}
