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
		searchHandleTask(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		searchHandleTask(request, response);
		
	}
	
	protected void searchHandleTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//プロパティファイル読み込み
			Properties prop = new Properties();
			getPropertiesFileInfo(request, response, prop);
			
			//検索ワードを取得
			String searchWord = request.getParameter("searchWord");
			
			//検索ワードがない場合、全件取得
			if(searchWord == null || searchWord.isBlank()) {
				pagingHandleOfAllTask(request, response);
			}else {
				// TODO　検索の場合、検索結果に該当するリストを取得した後にページング処理を行う必要がある
				List<TodoInfo> searchedResultTask = new ArrayList<TodoInfo>();
				//検索に一致したタスクを取得
				searchedResultTask = EditDataDao.getSearchedTask(searchWord);
				//検索結果がなかった場合メッセージ表示
				if(searchedResultTask.size() == 0) {
					String errorMessage = prop.getProperty("notFound.error");
					request.setAttribute("errorMessage", errorMessage);
					pagingHandleOfAllTask(request, response);
				}
				pagingHandleOnlyMatchTask(request, response, searchedResultTask);
				request.setAttribute("searchWord", searchWord);
				RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
				dispatcher.forward(request, response);
			}
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	
	//検索結果を一覧に表示した際のページネーション生成
	protected static void pagingHandleOnlyMatchTask(HttpServletRequest request, HttpServletResponse response, List<TodoInfo> searchedResultTask) throws ServletException, IOException{
		
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
		totalTaskCount = searchedResultTask.size();
		totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
		
		//そのページに表示する最初の行番号を取得
		int start = (pageNum - 1) * solidTaskCount;
		//そのページに表示する最後の行番号を取得
		//この2つのうち最小のほうがそのページに表示するリストの数となる
		//第一引数はそのページの要素数に限らず、+51ずつの値となるが、第二引数はそのページに表示するデータの要素数を表す
		//例えば、120件取得した場合、第一引数は151、第二引数は120を取得する
		int end = Math.min(pageNum * solidTaskCount - 1, searchedResultTask.size());
		
		//検索結果が保存されているリストを取得
		//TODO これだともしデータが120件とかの場合、存在しないインデックス151まで返そうとするので例外がスローされる
		pageByPageTaskList = searchedResultTask.subList(start, end);
		
		request.setAttribute("todoList", pageByPageTaskList);
		request.setAttribute("totalPageCount", totalPageCount);
		request.setAttribute("currentPage", currentPage);
		
	}
}
