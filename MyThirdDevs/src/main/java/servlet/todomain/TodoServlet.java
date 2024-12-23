package servlet.todomain;

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
 * @see全てのサーブレットクラスのスーパークラス
 * @see共通処理のエラーハンドリング処理とタスク一覧取得処理をもつ
 * 
 */

public class TodoServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//エラーハンドリング処理
	//ManageExceptionがcatchされた場合の処理
	protected void errorHandle(HttpServletRequest request, HttpServletResponse response, ManageException e) throws ServletException, IOException {
		//エラーか例外が発生した際に処理を行うクラスのインスタンス		
		ErrorHandle errorHandle = new ErrorHandle();
		String errorMessage = errorHandle.errorHandle(e);
		request.setAttribute("errorMessage", errorMessage);
		pagingHandleOfAllTask(request, response);
	}
	
	
	//TODO 名称変更すべき　ページネーションを生成する
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
	
	//TODO 名前要変更　特定の条件で絞られたデータに対してページング処理を行う
	protected void commonPagingProcess(HttpServletRequest request, HttpServletResponse response, List<TodoInfo> resultTask) throws ServletException, IOException{
		//現在のページ番号を取得
		String tmpPageNum = request.getParameter("pageNum");
		//ページング処理クラスのインスタンス
		TodopagingProcess todopagingPro = new TodopagingProcess();
		TodoPagingInfo<TodoInfo> todoPagingInfo = todopagingPro.pagingHandleOfSpecificTask(tmpPageNum, resultTask);
		request.setAttribute("totalPageCount", todoPagingInfo.getTotalPageCount());
		request.setAttribute("currentPage", todoPagingInfo.getCurrentPage());
		request.setAttribute("todoList", todoPagingInfo.getPageByPageListData());
	}
	
	//フォワード
	protected void forwardToTodoList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			List<Integer> favoriteTaskIdList = EditDataDao.getFavoriteTaskId();
			request.setAttribute("favoriteTaskIdList", favoriteTaskIdList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
