package servlet.todomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.common.ReadPropertiesFile;
import model.common.TodoInfo;
import model.dao.EditDataDao;
import model.exception.ManageException;
import servlet.TodoServlet;

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
	
	//検索機能
	protected void searchHandleTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//プロパティファイル読み込み
			Properties prop = new Properties();
			ReadPropertiesFile rpf = new ReadPropertiesFile();
			rpf.getPropertiesFileInfo(request, response, prop);
			
			//検索ワードを取得
			String searchWord = request.getParameter("searchWord");
			//並べかえの項目を取得
			String tHeaderParameter = request.getParameter("tHeaderParameter");
			//お気に入りで検索する際のフラグを取得
			boolean filteringFavorite = Boolean.valueOf(request.getParameter("filteringFavorite"));
			//お気に入りで検索をかけた場合、それだけを取得
			if(filteringFavorite) {
				List<TodoInfo> onlyFavoriteTaskList = EditDataDao.getOnlyFavoriteTaskList();
				request.setAttribute("todoList", onlyFavoriteTaskList);
				request.setAttribute("isFilteringFavorite", true);
				pagingHandleOfSpecificTask(request, response, onlyFavoriteTaskList);
				forwardToTodoList(request, response);
				return;
			}
			
			//検索ワードがない場合、全件取得
			if(searchWord == null || searchWord.isBlank()) {
				request.setAttribute("tHeaderParameter", tHeaderParameter);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/TodoOrderingServlet");
				dispatcher.forward(request, response);
			}else {
				//検索結果を格納するリスト
				List<TodoInfo> searchedResultTask = new ArrayList<TodoInfo>();
				//検索に一致したタスクを取得
				searchedResultTask = EditDataDao.getSearchedTask(searchWord);
				//検索結果がなかった場合メッセージ表示
				if(searchedResultTask.size() == 0) {
					String errorMessage = prop.getProperty("notFound.error");
					request.setAttribute("errorMessage", errorMessage);
					pagingHandleOfAllTask(request, response);
				//検索結果があった場合、その結果をもとに生成するページネーション処理を行う
				}else {
					pagingHandleOfSpecificTask(request, response, searchedResultTask);
					request.setAttribute("searchWord", searchWord);
				}
				forwardToTodoList(request, response);					
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
