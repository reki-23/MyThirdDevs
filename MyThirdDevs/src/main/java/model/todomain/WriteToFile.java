package model.todomain;


/*
 * 
 * ファイルへの書き込みクラス
 * 
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.ServletException;

import model.common.TodoInfo;
import model.dao.EditDataDao;
import model.exception.ManageException;


public class WriteToFile {
	//ファイルにタスク内容を書きこむクラス
	public void writeToCsv(String downloadFileToPath, List<TodoInfo> registeredTask) throws ManageException{
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(downloadFileToPath))){
			String header = "No,ステータス,分類,タスク名,タスク概要,作成日時,更新日時,作成者";
			bw.write(header);
			for(TodoInfo info : registeredTask) {
				bw.newLine();
				bw.write(info.toString());
			}
			bw.flush();
		}catch(IOException e) {
			throw new ManageException("EM002", e);
		}	
	}
	
	
	//フィルターをかけずにCSVファイルを出力
	public String exportCsvFile(WriteToFile writter, Path downloadDir) throws ServletException, IOException, ManageException{
		//現在登録されているタスクを取得
		List<TodoInfo> registeredTaskList = EditDataDao.getRegisteredTask();
		//ダウンロードしたいファイルへのパス
		String downloadFileToPath = downloadDir.resolve("task.csv").toString();
		//ファイルへの書き込み
		writter.writeToCsv(downloadFileToPath, registeredTaskList);
		return downloadFileToPath;
		
	}
	
	
	//フィルターをかけてCSVファイルを出力
	public String exportCsvFileWithFilter(WriteToFile writter, Path downloadDir, TodoInfo filteredTask) throws ServletException, IOException, ManageException  {
		//フィルター後のデータを取得
		List<TodoInfo> filteredTaskList = EditDataDao.getFilteredTaskList(filteredTask);
		//ダウンロードしたいファイルへのパス
		String downloadFileToPath = downloadDir.resolve("task_filter.csv").toString();
		//フィルター後のデータを書き込むクラスに渡す
		writter.writeToCsv(downloadFileToPath, filteredTaskList);
		return downloadFileToPath;
	}
}
