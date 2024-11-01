package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.TodoInfo;
import exception.ManageException;

/*
 * 一括登録時に渡されるファイルの内容を処理するクラス
 */
public class ReadFile {
	public List<TodoInfo> readCsvFile(String fileName) throws ManageException{
		
		//読みこんだ内容を保存するリスト
		List<TodoInfo> readTodoList = new ArrayList<TodoInfo>();
		//受けとったファイルをカンマ区切りで読み込み
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
			readTodoList = br
					.lines()
					.skip(1)
					.map(data -> data.split(","))
					.map(fields -> {
						try{
							separatedTodoInfoWithComma(fields);
						}catch(ManageException e) {
							throw new RuntimeException(e.getMessageId());
						}
						return new TodoInfo.Builder().build();
					}).collect(Collectors.toList());
			
		}catch(RuntimeException e) {
			String errorMessageId = e.getMessage();
			throw new ManageException(errorMessageId, e);
		}catch(IOException e) {
			
		}
		
		return null;
	}
	
	
	private TodoInfo separatedTodoInfoWithComma(String[] fields) throws ManageException{
		
		//No
		int id = DataValidator.returnValidId(fields);
		//ステータス
		String status = DataValidator.returnValidStatus(fields);
		//種別
		String classification = fields[2];
		//タスク名
		String task = fields[3];
		//タスク詳細
		String description = fields[4];
		//作成者
		String creator = fields[5];
		
		return new TodoInfo.Builder().with(todo -> {
					todo.id = id;
					todo.status = status;
					todo.classification = classification;
					todo.task = task;
					todo.description = description;
					todo.creator = creator;
					}).build();
	}
}
