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
		pagingHandleOfAllTask(request, response);
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
			
			// TODO　検索の場合、検索結果に該当するリストを取得した後にページング処理を行う必要がある
			List<TodoInfo> searchedResultTask = new ArrayList<TodoInfo>();
			//検索ワードがない場合、全件取得
			if(searchWord == null || searchWord.isBlank()) {
				searchedResultTask = EditDataDao.getRegisteredTask();
				pagingHandleOfAllTask(request, response);
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
				RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
				dispatcher.forward(request, response);
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
