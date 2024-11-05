package servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import model.WriteToFile;

/**
 * Servlet implementation class TodoExportServlet
 */
@WebServlet("/TodoExportServlet")
public class TodoExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			String export = request.getParameter("export-csv");
			
			if(export != null) {
				WriteToFile writter = new WriteToFile();
				
				//現在登録されているタスクを取得
				List<TodoInfo> registeredTask = EditDataDao.getRegisteredTask();
				
				//ダウンロードしたいファイルへのパス
				String downloadsToPath = getServletContext().getRealPath("/downloads");
				Path downloadDir = Paths.get(downloadsToPath);
				System.out.println(downloadDir);
				if(!Files.exists(downloadDir)) {
					Files.createDirectory(downloadDir);
				}
				String downloadFileToPath = downloadDir.resolve("info.csv").toString();
				
				//ファイルへの書き込み
				writter.writeToCsv(downloadFileToPath, registeredTask);
				
			}
			
		}catch(Exception e) {
			
		}
		
	}

}
