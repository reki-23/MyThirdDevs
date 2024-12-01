package servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EditDataDao;
import exception.ManageException;

/**
 * タスク登録用サーブレット
 */
@WebServlet("/TodoFavoriteTaskRegisterServlet")
@MultipartConfig
public class TodoFavoriteTaskRegisterServlet extends TodoServlet{
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
					
			//プロパティファイル読み込み
			Properties prop = new Properties();
			getPropertiesFileInfo(request, response, prop);
			
			//選択されたタスクidを取得=個別削除時
			String selectedFavIds = request.getParameter("selectedFavIds");
			
			//お気に入り登録処理
			if(selectedFavIds != null && !selectedFavIds.isBlank()) {
				int favoriteTaskId = Integer.valueOf(selectedFavIds);
				if(EditDataDao.registerFavoriteTask(favoriteTaskId)) {
					request.setAttribute("favoriteSubmitMessage", prop.getProperty("task.favorite.submit"));
				}else {
					request.setAttribute("favoriteCancelMessage", prop.getProperty("task.favorite.cancel"));					
				}
				pagingHandleOfAllTask(request, response);
				forwardToTodoList(request, response);
			}
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
