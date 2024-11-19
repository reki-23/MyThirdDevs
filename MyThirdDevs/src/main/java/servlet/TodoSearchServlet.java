package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;

/**
 * Servlet implementation class TodoExportServlet
 */
@WebServlet("/TodoSearchServlet")
public class TodoSearchServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//検索ワードを取得
			String searchWord = request.getParameter("searchWord");
			
			List<TodoInfo> searchedResultTask = new ArrayList<TodoInfo>();
			//検索ワードがない場合、全件取得
			if(searchWord == null || searchWord.isBlank()) {
				searchedResultTask = EditDataDao.getRegisteredTask();
			}else {
				//検索に一致したタスクを取得
				searchedResultTask = EditDataDao.getSearchedTask(searchWord);				
			}
			
			request.setAttribute("todoList", searchedResultTask);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
