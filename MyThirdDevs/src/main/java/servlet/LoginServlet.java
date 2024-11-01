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
import javax.servlet.http.HttpSession;

import common.CommonMessage;
import exception.ManageException;
import exception.PropertiesFileNotFoundException;

/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		Properties prop = new Properties();
		
		//セッションの取得
		HttpSession session = request.getSession();
		
		//プロパティファイル読み込み
		try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
			if(input == null) {
				//読み込み失敗時
				throw new ManageException("EM001", new PropertiesFileNotFoundException());
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
			
			//セッションスコープにユーザー情報を保存
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			
			if(username.isEmpty() || username == null || password.isEmpty() || password == null) {
				//メッセージ表示
				request.setAttribute("loginError", loginRequired);
				dispatcherToLoginJsp(request, response);
			}else if(!username.equals(requiredUserName) || !password.equals(requiredPassword)) {
				//メッセージ表示
				request.setAttribute("loginError", loginDenied);
				dispatcherToLoginJsp(request, response);
			}else {
				//正常にログインできた場合、次のサーブレットへ
				dispatcherToTopPageJSP(request, response);
			}
			
		}catch(ManageException e) {
			//発生した例外のIDに対応したメッセージを取得する
			CommonMessage common = new CommonMessage();
			request.setAttribute("loginError", common.getCommonMessage(e.getMessageId()));
			dispatcherToLoginJsp(request, response);
		}	
	}
	
	//LoginJSPへフォワード
	private static void dispatcherToLoginJsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/login.jsp");
		dispatcher.forward(request, response);
	}
	
	//TopPageJSPへフォワード
	private static void dispatcherToTopPageJSP(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/topPage.jsp");
		dispatcher.forward(request, response);
	}
}
