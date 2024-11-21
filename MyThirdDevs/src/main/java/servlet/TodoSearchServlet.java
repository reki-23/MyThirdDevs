package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;

/**
 * Servlet implementation class TodoExportServlet
 */
@WebServlet("/TodoSearchServlet")
public class TodoSearchServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pagingHandle();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//プロパティファイル読み込み
			Properties prop = new Properties();
			getPropertiesFileInfo(request, response, prop);
			
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
			
			//検索結果がなかった場合メッセージ表示
			if(searchedResultTask.size() == 0) {
				String errorMessage = prop.getProperty("notFound.error");
				request.setAttribute("errorMessage", errorMessage);
			}else {
				request.setAttribute("todoList", searchedResultTask);				
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
			dispatcher.forward(request, response);
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	//ページネーションを生成する＝doGet()で呼びだされる
	protected List<TodoInfo> pagingHandle() {
		//TODO ページ総数が1ページの場合
		//TODO ページ総数がNページの場合で、現在のページが1ページの場合
		//TODO ページ総数がNページの場合で、現在のページがNページの場合
		//TODO ページ総数がNページの場合で、現在のページが上記以外のページの場合
		
		//TODO　1ページに50件のタスクを表示するようにする
		/*
		 * 【必要な情報】
		 * 現在のページ
		 * タスク総数＝全件表示している場合は、EditDao.getRegisteredTask()の取得結果の件数がタスク総数にあたる
		 * 　　　　　　＝条件で検索し、特定のタスクのみを表示している場合は、EditDao.getSearchedTask（）の取得結果の件数がタスク総数にあたる
		 * タスク総数を50で割った数＝全ページ数
		 */
		
		//現在のページ・タスク総数・必要なページ数を初期化
		int currentPage = 1;
		int totalTaskCount = 0;
		int totalPageCount = 1;
		
		
		
		//そのページに表示するリストを返す
		return null;
	}
}
