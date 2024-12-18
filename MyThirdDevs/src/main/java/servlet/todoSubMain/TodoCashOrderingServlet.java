package servlet.todoSubMain;

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
import exception.ManageException;
import servlet.todoMain.TodoSearchServlet;
import servlet.todoMain.TodoServlet;

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
		RequestDispatcher dispatcher = request.getRequestDispatcher("/todo/todoCashList.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
	}
	
	protected void getOrderedHandleTask(HttpServletRequest request, HttpServletResponse response, String tHeaderParameter) throws ServletException, IOException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//並べかえの処理を行わない場合
			if(tHeaderParameter == null || tHeaderParameter.isBlank()) {
				pagingHandleOfAllCashTask(request, response);
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
	
	
	//ページネーションを生成する＝doGet()で呼びだされる
	protected void pagingHandleOfAllCashTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try {
			//ページごとに表示するタスクを格納したリスト
			List<TodoInfo> pageByPageTaskList = new ArrayList<>();
			//1ページに表示するタスク数
			final int solidTaskCount = 50;
			//デフォルトのページ番号
			int pageNum = 1;
			//現在のページ番号を取得
			String tmpPageNum = request.getParameter("pageNum");				
			if(tmpPageNum != null){
				pageNum = Integer.parseInt(tmpPageNum); //あとで例外処理を追加
			}
			
			//現在のページ・タスク総数・必要なページ数を初期化
			int currentPage = 1;
			int totalTaskCount = 0;
			int totalPageCount = 1;
			
			//タスク件数・ページ数計算
			currentPage = pageNum;
			totalTaskCount = EditDataDao.getCashedTask().size();
			totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
			
			pageByPageTaskList = EditDataDao.getSpecifyColumnTask(pageNum, solidTaskCount);
			request.setAttribute("todoCashList", pageByPageTaskList);
			request.setAttribute("totalPageCount", totalPageCount);
			request.setAttribute("currentPage", currentPage);
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
}
