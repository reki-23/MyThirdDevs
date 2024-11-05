package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
			
			WriteToFile writter = new WriteToFile();
			
			//現在登録されているタスクを取得
			List<TodoInfo> registeredTaskList = EditDataDao.getRegisteredTask();
			
			//ダウンロードしたいファイルへのパス
			String downloadsToPath = getServletContext().getRealPath("/downloads");
			Path downloadDir = Paths.get(downloadsToPath);
			if(!Files.exists(downloadDir)) {
				Files.createDirectories(downloadDir);
			}
			String downloadFileToPath = downloadDir.resolve("task.csv").toString();
			
			//ファイルへの書き込み
			writter.writeToCsv(downloadFileToPath, registeredTaskList);
			
			//レスポンスヘッダーの設定
			File downloadedFile = new File(downloadFileToPath);
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "task.csv" + "\"");
			response.setContentLengthLong(downloadedFile.length());
			//ダウンロードするファイルを読み込み、出力ストリームを生成し一括書き込み後にコミット
			try(FileInputStream in = new FileInputStream(downloadedFile);
				ServletOutputStream out = response.getOutputStream()){
				in.transferTo(out);
				out.flush();
			}
				
			
		}catch(Exception e) {
			
		}
		
	}

}
