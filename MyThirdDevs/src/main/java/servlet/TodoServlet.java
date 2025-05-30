package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.common.ErrorHandle;
import model.common.TodoInfo;
import model.common.TodoPagingInfo;
import model.dao.EditDataDao;
import model.exception.ManageException;
import model.todomain.TodopagingProcess;

/**
 * 
 * @memo全てのサーブレットクラスのスーパークラス
 * @memo共通処理のエラーハンドリング処理とタスク一覧取得処理をもつ
 * 
 */

public class TodoServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//エラーハンドリング処理
	protected void errorHandle(HttpServletRequest request, HttpServletResponse response, ManageException e) throws ServletException, IOException {
		//エラーか例外が発生した際に処理を行うクラスのインスタンス		
		ErrorHandle errorHandle = new ErrorHandle();
		String errorMessage = errorHandle.errorHandle(e);
		request.setAttribute("errorMessage", errorMessage);
		//TODO エラー専用ページへ遷移
	}
	
	
	//全タスクのページネーションを生成する
	protected void pagingHandleOfAllTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			//ページング処理を行うクラスのインスタンスを生成
			TodopagingProcess todoPagingPro = new TodopagingProcess();
			String tmpPageNum = request.getParameter("pageNum");
			//ページング処理で使用するデータを管理するクラスのインスタンス
			TodoPagingInfo<TodoInfo> todoPagingInfo = todoPagingPro.pagingHandleOfAllTask(tmpPageNum);
			
			request.setAttribute("todoList", todoPagingInfo.getPageByPageListData());
			request.setAttribute("totalPageCount", todoPagingInfo.getTotalPageCount());
			request.setAttribute("currentPage", todoPagingInfo.getCurrentPage());
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	//特定の条件で絞られたデータに対してページング処理を行う
	protected void pagingHandleOfSpecificTask(HttpServletRequest request, HttpServletResponse response, List<TodoInfo> resultTask) throws ServletException, IOException{
		//現在のページ番号を取得
		String tmpPageNum = request.getParameter("pageNum");
		//ページング処理クラスのインスタンス
		TodopagingProcess todopagingPro = new TodopagingProcess();
		TodoPagingInfo<TodoInfo> todoPagingInfo = todopagingPro.pagingHandleOfSpecificTask(tmpPageNum, resultTask);
		request.setAttribute("totalPageCount", todoPagingInfo.getTotalPageCount());
		request.setAttribute("currentPage", todoPagingInfo.getCurrentPage());
		request.setAttribute("todoList", todoPagingInfo.getPageByPageListData());
	}
	
	
	//ゴミ箱内のデータでページネーションを生成する
	protected void pagingHandleOfAllCashTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			//現在のページ番号を取得
			String tmpPageNum = request.getParameter("pageNum");
			//ページング処理を行うクラスのインスタンス
			TodopagingProcess todopagingPro = new TodopagingProcess();
			TodoPagingInfo<TodoInfo> todoPagingInfo = todopagingPro.pagingHandleOfAllCashTask(tmpPageNum);
			request.setAttribute("todoCashList", todoPagingInfo.getPageByPageListData());
			request.setAttribute("totalPageCount", todoPagingInfo.getTotalPageCount());
			request.setAttribute("currentPage", todoPagingInfo.getCurrentPage());
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	
	//ゴミ箱内で特定の条件で絞られたデータに対してページング処理を行う
	protected void pagingHandleOfSpecificCashTask(HttpServletRequest request, HttpServletResponse response, List<TodoInfo> resultTask) throws ServletException, IOException{
		//現在のページ番号を取得
		String tmpPageNum = request.getParameter("pageNum");
		//ページング処理クラスのインスタンス
		TodopagingProcess todopagingPro = new TodopagingProcess();
		TodoPagingInfo<TodoInfo> todoPagingInfo = todopagingPro.pagingHandleOfSpecificTask(tmpPageNum, resultTask);
		request.setAttribute("totalPageCount", todoPagingInfo.getTotalPageCount());
		request.setAttribute("currentPage", todoPagingInfo.getCurrentPage());
		request.setAttribute("todoCashList", todoPagingInfo.getPageByPageListData());
	}
	
	
	//todoList.jspへフォワード
	protected void forwardToTodoList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			//お気に入りタスクのidを取得した結果を保持するリスト
			List<Integer> favoriteTaskIdList = EditDataDao.getFavoriteTaskId();
			request.setAttribute("favoriteTaskIdList", favoriteTaskIdList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	//todoCashList.jspへフォワード
	protected void forwardTodoCashList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoCashList.jsp");
		dispatcher.forward(request, response);
	}
}
