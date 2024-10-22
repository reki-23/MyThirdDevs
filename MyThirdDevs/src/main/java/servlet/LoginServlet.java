package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.PropertiesFileNotFoundException;

/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			Properties prop = new Properties();
			
			//プロパティファイル読み込み
			try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
				if(input == null) {
					throw new PropertiesFileNotFoundException();
				}
				
				try(InputStreamReader reader = new InputStreamReader(input, "UTF-8")){
					prop.load(reader);
				}
				
				//ログイン情報取得
				final String requiredUserName = prop.getProperty("login.name");
				final String requiredPassword = prop.getProperty("login.pass");
				final String loginRequired = prop.getProperty("login.required");
				final String loginDenied = prop.getProperty("login.denied");
				String username = request.getParameter("user");
				String password = request.getParameter("pass");
				
				if(username.isEmpty() || username == null || password.isEmpty() || password == null) {
					//メッセージ表示
					request.setAttribute("loginError", loginRequired);
					dispatcherToJsp(request, response);
				}else if(!username.equals(requiredUserName) || !password.equals(requiredPassword)) {
					//メッセージ表示
					request.setAttribute("loginError", loginDenied);
					dispatcherToJsp(request, response);
				}else {
					//正常にログインできた場合、次のサーブレットへ
				}
				
			}catch(PropertiesFileNotFoundException e) {
				request.setAttribute("loginError", "プロパティファイル読み込めてない");
				dispatcherToJsp(request, response);
			}
			
		}catch(Exception e) {
			
		}
	}
	
	private static void dispatcherToJsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/login.jsp");
		dispatcher.forward(request, response);
	}
}
