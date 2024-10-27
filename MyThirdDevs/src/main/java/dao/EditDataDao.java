package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import common.ManageException;
import common.TodoInfo;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todoList VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todoList";
	private static final String getSql = "SELECT * FROM todoList";
	private static final String individualDeleteSql = "DELETE FROM todoList WHERE id = ?";
	
	
	//タスク取得
	public List<TodoInfo> getRegisteredTask() throws ManageException{
		
		List<TodoInfo> todoList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(getSql);
			ResultSet rs = ps.executeQuery()){
			
			while(rs.next()) {
				int id = rs.getInt(1);
				String status = rs.getString(2);
				String classification = rs.getString(3);
				String task = rs.getString(4);
				String description = rs.getString(5);
				LocalDateTime createDateTime = LocalDateTime.parse(rs.getString(6));
				LocalDateTime updateDateTime = LocalDateTime.parse(rs.getString(7));
				String creator = rs.getString(8);
				
				todoList.add(new TodoInfo.Builder().with(todo -> {
					todo.id = id;
					todo.status = status;
					todo.classification = classification;
					todo.task = task;
					todo.description = description;
					todo.createDateTime = createDateTime;
					todo.updateDateTime = updateDateTime;
					todo.creator = creator;
				}).build());
			}
			
			return todoList;
			
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
	
	//タスク新規登録
	public void registerNewTask(List<TodoInfo> newTaskList) throws ManageException{
		
		//いま登録するときのJSPが完成していないので、登録のテスト用に仮でここでデータを定義しておく。
		List<TodoInfo> registeredTaskList = new ArrayList<>();
		registeredTaskList.add(new TodoInfo.Builder().with(todo -> {
			todo.id = 100;
			todo.status = "finish";
			todo.classification = "work";
			todo.task = "create an Activity";
			todo.description = "aaa";
			todo.createDateTime = LocalDateTime.now();
			todo.updateDateTime = LocalDateTime.now();
			todo.creator = "reki";
		}).build());
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(insertSql)){
			
			for(TodoInfo todo : newTaskList) {
				ps.setInt(1, todo.getId());
				ps.setString(2, todo.getStatus());
				ps.setString(3, todo.getClassification());
				ps.setString(4, todo.getTask());
				ps.setString(5, todo.getDescription());
				ps.setString(6, String.valueOf(todo.getCreateDateTime()));
				ps.setString(7, String.valueOf(todo.getUpdateDateTime()));
				ps.setString(8, todo.getCreator());
				//上記のパラメータをバッチにセット
				ps.addBatch();
			}
			//一括実行＝一括登録
			ps.executeBatch();
		}catch(SQLException e) {
			
		}
	}
	
	
	/*
	 * 1つずつ消す場合と一括で消す場合で処理を分ける
	 */
	//タスク削除＝一括削除
	public boolean bulkDeleteTask() throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(deleteSql)){
			//削除件数
			int deleteCount = ps.executeUpdate();
			if(deleteCount > 0) {
				//trueなら削除成功
				return true;
			}else {
				//falseなら削除失敗
				return false;				
			}
			
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
	
	//タスク個別削除処理
	public boolean individualDeleteTask() throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(deleteSql)){
				
			/*
			 * 押下されたタスクのIDを受取り削除する処理
			 */
			
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
}
