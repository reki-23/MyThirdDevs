package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonMessage;
import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;
import exception.PropertiesFileNotFoundException;

/**
 * 
 * @see全てのサーブレットクラスのスーパークラス
 * @see共通処理のエラーハンドリング処理とタスク一覧取得処理をもつ
 * 
 */

public class TodoServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//プロパティファイルを読み込む
	protected void getPropertiesFileInfo(HttpServletRequest request, HttpServletResponse response, Properties prop) throws ServletException, IOException{
		try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
			if(input == null) {
				//読み込み失敗時
				throw new ManageException("EM001", new PropertiesFileNotFoundException());
			}
			try(InputStreamReader reader = new InputStreamReader(input, "UTF-8")){
				prop.load(reader);
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	
	//エラーハンドリング処理
	//ManageExceptionがcatchされた場合の処理
	protected static void errorHandle(HttpServletRequest request, HttpServletResponse response, ManageException e) throws ServletException, IOException {
		CommonMessage commonMessage = new CommonMessage();
		String errorMessage = commonMessage.getCommonMessage(e.getMessageId());
		request.setAttribute("errorMessage", errorMessage);
		pagingHandleOfAllTask(request, response);
	}
	
	
	//ページネーションを生成する＝doGet()で呼びだされる
	protected static void pagingHandleOfAllTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
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
			totalTaskCount = EditDataDao.getRegisteredTask().size();
			totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
			
			pageByPageTaskList = EditDataDao.getSpecifyColumnTask(pageNum, solidTaskCount);
			request.setAttribute("todoList", pageByPageTaskList);
			request.setAttribute("totalPageCount", totalPageCount);
			request.setAttribute("currentPage", currentPage);
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	
	//フォワード
	protected static void forwardToTodoList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
