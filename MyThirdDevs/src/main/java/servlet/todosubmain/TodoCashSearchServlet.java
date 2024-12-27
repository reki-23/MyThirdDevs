package servlet.todosubmain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
@WebServlet("/TodoCashSearchServlet")
public class TodoCashSearchServlet extends TodoServlet {
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
			
			//検索ワードがない場合、全件取得
			if(searchWord == null || searchWord.isBlank()) {
				pagingHandleOfAllCashTask(request, response);
				forwardTodoCashList(request, response);
			}else {
				//検索結果を格納するリスト
				List<TodoInfo> searchedResultTask = new ArrayList<TodoInfo>();
				//検索に一致したタスクを取得
				searchedResultTask = EditDataDao.getSearchedCashTask(searchWord);
				//検索結果がなかった場合メッセージ表示
				if(searchedResultTask.size() == 0) {
					String errorMessage = prop.getProperty("notFound.error");
					request.setAttribute("errorMessage", errorMessage);
					pagingHandleOfAllCashTask(request, response);
				//検索結果があった場合、その結果をもとに生成するページネーション処理を行う
				}else {
					pagingHandleOfSpecificCashTask(request, response, searchedResultTask);
					request.setAttribute("searchWord", searchWord);
				}
				forwardTodoCashList(request, response);					
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
