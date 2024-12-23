package servlet.todomain;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.common.ReadPropertiesFile;
import model.dao.EditDataDao;
import model.exception.ManageException;

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
			ReadPropertiesFile rpf = new ReadPropertiesFile();
			rpf.getPropertiesFileInfo(request, response, prop);
			
			//選択されたタスクidを取得=個別削除時
			String selectedFavIds = request.getParameter("selectedFavIds");
			
			//お気に入り登録処理
			if(selectedFavIds != null && !selectedFavIds.isBlank()) {
				int favoriteTaskId = Integer.valueOf(selectedFavIds);
				if(EditDataDao.registerFavoriteTask(favoriteTaskId)) {
					request.setAttribute("favoriteSubmitMessage",MessageFormat.format(prop.getProperty("task.favorite.submit"), favoriteTaskId));
				}else {
					request.setAttribute("favoriteCancelMessage", MessageFormat.format(prop.getProperty("task.favorite.cancel"), favoriteTaskId));					
				}
				pagingHandleOfAllTask(request, response);
				forwardToTodoList(request, response);
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
