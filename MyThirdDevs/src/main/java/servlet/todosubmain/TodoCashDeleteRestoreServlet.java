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
@WebServlet("/TodoCashDeleteRestoreServlet")
public class TodoCashDeleteRestoreServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			String menu_option = request.getParameter("menu-option");
			if(menu_option.equals("menu-option-1")) {
				//復元処理
				//TODO 復元の場合は、ゴミ箱内のデータを削除しタスク一覧をロールバックする方式
				//TODO ゴミ箱内のデータはunique_noという主キーをもつので、タスク一覧とのテーブル定義が異なるため副問い合わせでINSERTできない
				System.out.println("通過確認");
			}else if(menu_option.equals("menu-option-2")) {
				//削除処理
				boolean bulkDeleteJudge = EditDataDao.bulkDeleteCashListTask();
				System.out.println(bulkDeleteJudge);
				request.setAttribute("deleteJudge", bulkDeleteJudge);
			}
			pagingHandleOfAllCashTask(request, response);
			forwardTodoCashList(request, response);
		}catch(ManageException e) {
			e.printStackTrace();
			errorHandle(request, response, e);
		}
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
	}
}
