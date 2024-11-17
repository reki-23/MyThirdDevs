package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import common.TodoInfo;
import exception.ManageException;
import exception.NoElementsOnFileException;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todolist VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todolist";
	private static final String getSql = "SELECT * FROM todolist";
	private static final String individualDeleteSql = "DELETE FROM todolist WHERE id = ?";
	private static final int invalidId = -1;
	private static final LocalDateTime invalidCreateDateTime = LocalDateTime.MIN;
	private static final LocalDateTime invalidUpdateDateTime = LocalDateTime.MIN;
//	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	
	//タスク取得
	public static List<TodoInfo> getRegisteredTask() throws ManageException{
		
		List<TodoInfo> todoList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(getSql);
			ResultSet rs = ps.executeQuery()){
			
			//データ取得
			while(rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String classification = rs.getString("classification");
				String task = rs.getString("task");
				String description = rs.getString("description");
				LocalDateTime createDateTime = rs.getTimestamp("createDateTime").toLocalDateTime();
				LocalDateTime updateDateTime = rs.getTimestamp("updateDateTime").toLocalDateTime();
				String creator = rs.getString("creator");
				
				//データを追加
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
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク個別登録、一括登録
	public static int registerNewTask(List<TodoInfo> newTaskList, boolean isBulkJudge) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(insertSql)){
			
			if(!isBulkJudge) {
				//個別登録
				TodoInfo newTask = newTaskList.get(0);
				ps.setInt(1, newTask.getId());
				ps.setString(2, newTask.getStatus());
				ps.setString(3, newTask.getClassification());
				ps.setString(4, newTask.getTask());
				ps.setString(5, newTask.getDescription());
				ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
				//これは更新日時なので、登録時点ではなく、タスク詳細画面でタスクを編集し、決定した時間を取得するようにする。
				//今は仮で、今の時間を取得する
				ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
				ps.setString(8, newTask.getCreator());
				int registeredCount = ps.executeUpdate();
				return registeredCount;
			}else {
				//一括登録
				for(TodoInfo newTask : newTaskList) {
					ps.setInt(1, newTask.getId());
					ps.setString(2, newTask.getStatus());
					ps.setString(3, newTask.getClassification());
					ps.setString(4, newTask.getTask());
					ps.setString(5, newTask.getDescription());
					ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
					ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); //ここは更新日時に変更する
					ps.setString(8, newTask.getCreator());
					ps.addBatch();
				}
				//一括実行（登録）
				int[] bulkRegisteredCount =  ps.executeBatch();
				return bulkRegisteredCount.length;
			}
		
		//整合性制約違反
		}catch(SQLIntegrityConstraintViolationException e) {
			throw new ManageException("EM009", e);
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク一括削除
	public static boolean bulkDeleteTask() throws ManageException{
		
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
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク個別削除
	public static boolean individualDeleteTask(List<Integer> deletedIdList) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(individualDeleteSql)){
				
			//押下されたタスクのIDを受取り削除する処理
			for(Integer deletedId : deletedIdList) {
				ps.setInt(1, deletedId);
				ps.addBatch();
			}
			//一括削除
			int[] indiDelCount = ps.executeBatch();
			if(indiDelCount.length > 0) {
				return true;
			}else {
				return false;
			}		
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("EM003", e);
		}
	}
	
	
	//フィルターをかけて一覧取得
	public static List<TodoInfo> getFilteredTaskList(TodoInfo filteredTask) throws ManageException{
		
		//TODO　ここでは全件取得してからフィルタリングをするのではなく、SQL側でフィルタリングの処理を行ってから取得する
		//TODO フィルターにかけたい値によってクエリを動的に生成する処理
		
		//フィルタ―後のDBから取得したデータを格納するリスト
		List<TodoInfo> filteredTaskList = new ArrayList<>();
		//動的クエリの生成準備
		StringBuilder queryBuilder = new StringBuilder("SELECT * FROM todolist WHERE 1=1");
		//取得したフィルターデータを格納する
		List<Object> parameters = new ArrayList<>();
		
		//有効な値の場合、フィルターにかけたいデータのみを取り出すクエリを生成する
		if(filteredTask.getId() != invalidId) {
			queryBuilder.append(" AND id = ?");
			parameters.add(filteredTask.getId());
		}
		if(filteredTask.getStatus() != null && !filteredTask.getStatus().isBlank()) {
			queryBuilder.append(" AND status = ?");
			parameters.add(filteredTask.getStatus());
		}
		if(filteredTask.getClassification() != null && !filteredTask.getClassification().isBlank()) {
			queryBuilder.append(" AND classification = ?");
			parameters.add(filteredTask.getClassification());
		}
		if(filteredTask.getTask() != null && !filteredTask.getTask().isBlank()) {
			queryBuilder.append(" AND task = ?");
			parameters.add(filteredTask.getTask());
		}
		if(filteredTask.getDescription() != null && !filteredTask.getDescription().isBlank()) {
			queryBuilder.append(" AND description = ?");
			parameters.add(filteredTask.getDescription());
		}
		if(filteredTask.getCreateDateTime() != null && filteredTask.getCreateDateTime() != invalidCreateDateTime) {
			queryBuilder.append(" AND createDateTime = ?");
			parameters.add(filteredTask.getCreateDateTime());
		}
		if(filteredTask.getUpdateDateTime() != null && filteredTask.getUpdateDateTime() != invalidUpdateDateTime) {
			queryBuilder.append(" AND updateDateTime = ?");
			parameters.add(filteredTask.getUpdateDateTime());
		}
		if(filteredTask.getCreator() != null && !filteredTask.getCreator().isBlank()) {
			queryBuilder.append(" AND creator = ?");
			parameters.add(filteredTask.getCreator());
		}
		
		//フィルター後の値(=parameters)を検索
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(queryBuilder.toString())){
			
			//フィルター後の値を上記で生成したクエリにセット
			for(int i = 0; i < parameters.size(); i++){
				ps.setObject(i + 1, parameters.get(i));
			}
			
			//生成したクエリ条件でDB内を検索
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					filteredTaskList.add(mapResultSetToTodoInfo(rs));
				}				
			}
			
			//1つも条件を満たさなかった場合例外
			if(filteredTaskList.size() == 0) {
				throw new ManageException("EM016", new NoElementsOnFileException());
			}
			
		}catch(RuntimeException e) {
			String errorMessageId = e.getMessage();
			throw new ManageException(errorMessageId, e);
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
		return filteredTaskList;
	}
	
	
	//DB内を検索し、インスタンスを返す
	private static TodoInfo mapResultSetToTodoInfo(ResultSet rs) throws RuntimeException{
		
		return new TodoInfo.Builder().with(dbData -> {
			try {
				dbData.id = rs.getInt("id");
				dbData.status = rs.getString("status");
				dbData.classification = rs.getString("classification");
				dbData.task = rs.getString("task");
				dbData.description = rs.getString("description");
				dbData.createDateTime = rs.getTimestamp("createDateTime").toLocalDateTime();
				dbData.updateDateTime = rs.getTimestamp("updateDateTime").toLocalDateTime();
				dbData.creator = rs.getString("creator");				
			}catch(SQLException e){
				try {
					throw new ManageException("EM003", e);					
				}catch(ManageException e2) {
					throw new RuntimeException(e2.getMessageId());
				}
			}
		}).build();
	}
}
