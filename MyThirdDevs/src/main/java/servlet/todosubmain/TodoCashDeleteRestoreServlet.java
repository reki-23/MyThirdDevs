package servlet.todosubmain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
			
			//クリックしたリンクの値を取得
			String menu_option = request.getParameter("menu-option");			

			if(menu_option.equals("menu-option-1")) {
				//一括復元処理
				boolean bulkRestoreJudge = EditDataDao.bulkRestoreCashList();
				request.setAttribute("restoreJudge", bulkRestoreJudge);
			}else if(menu_option.equals("menu-option-2")) {
				//削除処理
				boolean bulkDeleteJudge = EditDataDao.bulkDeleteCashList();
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
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//クリックしたボタン(個別復元か個別削除か)の値を取得
			String menu_option = request.getParameter("menu-option");
			String restore_cancel = request.getParameter("restore_cancel");
			
			if(menu_option.equals("menu-option-3") && restore_cancel == null) {
				//個別復元処理
				//個別復元するidを取得
				String[] selectedRestoreTaskIds = request.getParameterValues("selectedRestoreIds");
				List<Integer> selectedRestoreTaskIdList = Arrays
						.stream(selectedRestoreTaskIds)
						.flatMap(id -> Arrays.stream(id.split(",")))
						.map(Integer::valueOf)
						.collect(Collectors.toList());
				//個別復元するidをListとしてDAOに送信
				boolean individualRestoreJudge = EditDataDao.individualRestoreTask(selectedRestoreTaskIdList);
				request.setAttribute("restoreJudge", individualRestoreJudge);
			}
			pagingHandleOfAllCashTask(request, response);
			forwardTodoCashList(request, response);
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
