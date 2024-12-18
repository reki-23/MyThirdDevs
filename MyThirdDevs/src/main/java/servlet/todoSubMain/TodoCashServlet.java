package servlet.todoSubMain;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;
import servlet.todoMain.TodoServlet;

/**
 * Servlet implementation class TodoCashServlet
 */
@WebServlet("/TodoCashServlet")
public class TodoCashServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//コンテキストメニューで押下したURLパラメータを取得
			String menu_option = request.getParameter("menu-option");
			
			//ゴミ箱を開く処理
			if(menu_option.equals("menu-option-1")) {
				//ゴミ箱の内容を取得してリクエスタにセットしてからフォワード
				List<TodoInfo> todoCashList = EditDataDao.getCashedTask();
				request.setAttribute("todoCashList", todoCashList);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/todo/todoCashList.jsp");
				dispatcher.forward(request, response);
			//ゴミ箱内を一括で削除する処理
			}else if(menu_option.equals("menu-option-2")) {
				request.setAttribute("deleteJudge", EditDataDao.bulkDeleteCashListTask());
				pagingHandleOfAllTask(request, response);
				forwardToTodoList(request, response);
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		
	}
}
