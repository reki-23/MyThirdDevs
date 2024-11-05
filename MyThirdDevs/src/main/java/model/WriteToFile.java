package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import common.TodoInfo;
import exception.ManageException;

public class WriteToFile {
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
}
