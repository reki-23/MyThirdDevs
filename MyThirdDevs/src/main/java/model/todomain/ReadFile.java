package model.todomain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.common.TodoInfo;
import model.exception.ManageException;
import model.validation.DataValidator;

/*
 * 一括登録時に渡されるファイルの内容を処理するクラス
 */

public class ReadFile {
	
	//ファイルを読み込む
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
							return separatedTodoInfoWithComma(fields);
						}catch(ManageException e) {
							throw new RuntimeException(e.getMessageId());
						}
					}).collect(Collectors.toList());
			
		}catch(RuntimeException e) {
			String errorMessageId = e.getMessage();
			throw new ManageException(errorMessageId, e);
		}catch(IOException e) {
			throw new ManageException("EM002", e);
		}
		
		return readTodoList;
	}
	
	
	//カンマで区切った各データをフィールドに持つインスタンスを返す
	private TodoInfo separatedTodoInfoWithComma(String[] fields) throws ManageException{
		
		//No
		int id = DataValidator.returnValidId(DataValidator.getFieldsSafely(fields, 0));
		//ステータス
		String status = DataValidator.returnValidStatus(DataValidator.getFieldsSafely(fields, 1));
		//種別
		String classification = DataValidator.getFieldsSafely(fields, 2);
		//タスク名
		String task = DataValidator.getFieldsSafely(fields, 3);
		//タスク詳細
		String description = DataValidator.getFieldsSafely(fields, 4);
		//作成者
		String creator = DataValidator.getFieldsSafely(fields, 5);
		
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
