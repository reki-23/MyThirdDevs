package servlet.todosubmain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.EditDataDao;
import model.exception.ManageException;
import servlet.TodoServlet;

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
				pagingHandleOfAllCashTask(request, response);
				forwardTodoCashList(request, response);
			//ゴミ箱内を一括で削除する処理
			}else if(menu_option.equals("menu-option-2")) {
				request.setAttribute("deleteJudge", EditDataDao.bulkDeleteCashList());
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
		//ゴミ箱内のページネーションを生成する処理呼ぶ
		pagingHandleOfAllCashTask(request, response);
		
		
	}
}
