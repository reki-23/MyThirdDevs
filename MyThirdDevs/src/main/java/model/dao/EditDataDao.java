package model.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.common.TodoInfo;
import model.exception.ManageException;
import model.exception.NoElementsOnFileException;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final int invalidId = -1;
	private static final LocalDateTime invalidCreateDateTime = LocalDateTime.MIN;
	private static final LocalDateTime invalidUpdateDateTime = LocalDateTime.MIN;	
	
	//タスク取得
	public static List<TodoInfo> getRegisteredTask() throws ManageException{
		
		List<TodoInfo> todoList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.getSql);
			ResultSet rs = ps.executeQuery()){
			//データ取得
			while(rs.next()) {
				todoList.add(mapResultSetToTodoInfo(rs));
			}
			return todoList;
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク個別登録、一括登録
	public static int registerNewTask(List<TodoInfo> newTaskList, boolean isBulkJudge) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.insertSql)){
			
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
				ps.setBoolean(9, false);
				return ps.executeUpdate();
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
					ps.setBoolean(9, false);
					ps.addBatch();
				}
				//一括実行（登録）
				return ps.executeBatch().length;					
			}
		//整合性制約違反
		}catch(SQLIntegrityConstraintViolationException | BatchUpdateException e) {
			throw new ManageException("EM009", e);
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
	}
	
	
	//お気に入りタスク登録
	public static boolean registerFavoriteTask(int favoriteTaskId) throws ManageException{
		try(Connection con = dbc.getConnection();
			PreparedStatement selectTodoListPs = con.prepareStatement(TaskQueries.selectIdSql);
			PreparedStatement selectFavTodoListPs = con.prepareStatement(TaskQueries.selectFavIdSql);
			PreparedStatement updateFavFlgPs = con.prepareStatement(TaskQueries.updateFavFlgSql)){
			
			//そのタスクidがすでにお気に入り登録されている場合、登録処理ではなく削除処理
			boolean isNotExists = true;
			selectFavTodoListPs.setInt(1, favoriteTaskId);
			try(ResultSet rs = selectFavTodoListPs.executeQuery()){
				while(rs.next()) {
					if(rs.getBoolean("isFavorite")) {
						isNotExists = false;
						break;
					}
				}
			}
			
			//お気に入りボタンが押されたことによって送信されるタスクidがお気に入りテーブル内に存在してない場合登録
			if(isNotExists) {
				//選択されたidをパラメータにセットし、そのidのタスクのお気に入りフラグをtrueに更新
				selectTodoListPs.setInt(1, favoriteTaskId);
				updateFavFlgPs.setBoolean(1, true);
				updateFavFlgPs.setInt(2, favoriteTaskId);
				if(updateFavFlgPs.executeUpdate() > 0) {
					return true;
				}
			//お気に入りボタンが押されたことによって送信されるタスクidがすでにお気に入りテーブル内に存在している場合削除
			}else {
				//すでにあるタスクidのお気に入りフラグをfalseに更新
				updateFavFlgPs.setBoolean(1, false);
				updateFavFlgPs.setInt(2, favoriteTaskId);
				if(updateFavFlgPs.executeUpdate() > 0) {
					return false;
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
		return false;
	}
	
	
	//お気に入りのタスクidを取得
	public static List<Integer> getFavoriteTaskId() throws ManageException{
		//お気に入りのタスクidを保持するリスト
		List<Integer> favoriteTaskIdList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.selectFavSql);
			ResultSet rs = ps.executeQuery()){
		
			while(rs.next()) {
				int favTaskId = rs.getInt("id");
				favoriteTaskIdList.add(favTaskId);
			}
			
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
		return favoriteTaskIdList;
	}
	
	
	//お気に入りのタスクのみを取得
	public static List<TodoInfo> getOnlyFavoriteTaskList() throws ManageException{
		//お気に入りのタスクのみを保持するリスト
		List<TodoInfo> onlyFavoriteTaskList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.selectFavSql);
			ResultSet rs = ps.executeQuery()){
		
			while(rs.next()) {
				onlyFavoriteTaskList.add(mapResultSetToTodoInfo(rs));
			}
			
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
		return onlyFavoriteTaskList;
	}
	
	
	//todolistでのタスク一括削除＝ゴミ箱への登録
	public static boolean bulkDeleteTask() throws ManageException{
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.deleteSql);
			PreparedStatement insertCashSqlPs = con.prepareStatement(TaskQueries.insertAllCashSql)){
			
			//todolistで削除されるタスクをキャッシュリストに保存
			insertCashSqlPs.addBatch();
			insertCashSqlPs.executeBatch();
			
			//削除件数
			int deleteCount = ps.executeUpdate();
			if(deleteCount != 0) {
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
	
	
	//todolistでのタスク個別削除＝ゴミ箱への登録
	public static boolean individualDeleteTask(List<Integer> deletedIdList) throws ManageException{
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.individualDeleteSql);
			PreparedStatement insertCashSqlPs = con.prepareStatement(TaskQueries.insertCashSql)){
			
			//todolist削除されるタスクをキャッシュリストに保存
			for(Integer deleteId : deletedIdList) {
				insertCashSqlPs.setInt(1, deleteId);
				insertCashSqlPs.addBatch();
			}
			insertCashSqlPs.executeBatch();
			
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
	
	
	//ゴミ箱内を一括削除＝完全に削除
	public static boolean bulkDeleteCashList() throws ManageException{
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.deleteCashSql)){
			
			//削除件数
			ps.addBatch();
			int deleteCount = ps.executeBatch().length;
			if(deleteCount > 0) {
				//trueなら削除成功
				return true;
			}else {
				//falseなら削除失敗
				return false;				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
	}
	
	
	//ゴミ箱内のデータを完全に復元
	public static boolean bulkRestoreCashList() throws ManageException{
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.restoreToQuery);
			PreparedStatement trancateCashPs = con.prepareStatement(TaskQueries.deleteCashSql)){
			
			//復元件数
			ps.addBatch();
			int restoreCount = ps.executeBatch().length;
			if(restoreCount > 0) {
				//trueなら復元成功
				//ゴミ箱内のデータは削除する
				trancateCashPs.addBatch();
				trancateCashPs.executeBatch();
				return true;
			}else {
				//falseなら復元失敗
				return false;				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
	}
	
	
	//ゴミ箱の中身を取得
	public static List<TodoInfo> getCashedTask() throws ManageException{
		List<TodoInfo> todoCashList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.getCashSql);
			ResultSet rs = ps.executeQuery()){
			//データ取得
			while(rs.next()) {
				todoCashList.add(mapResultSetToTodoInfo(rs));
			}
			return todoCashList;
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク検索
	public static List<TodoInfo> getSearchedTask(String keyWord) throws ManageException{	
		//TODO 検索ワードが"時刻のとき"で分岐する必要がある
		List<TodoInfo> searchedResultTask = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder(TaskQueries.dynamicQuery);
		
		//時刻かどうかを判定するフラグ
		boolean isDateTime = false;
		
		//検索ワードをformatterの形式に変換できれば、日付けを含んだクエリを生成する
		try {
			LocalDateTime.parse(keyWord, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			isDateTime = true;
		}catch(Exception e) {
			isDateTime = false;
		}
		
		//時刻が含まれている場合は、時刻を含んだクエリを生成する
		if(isDateTime) {
			queryBuilder.append(" AND id = ?").append(" OR status = ?").append(" OR classification = ?")
						.append(" OR task = ?").append(" OR description = ?").append(" OR createDateTime = ?")
						.append(" OR updateDateTime = ?").append(" OR creator = ?");
		//検索ワードが時刻出ない場合、時刻は検索対象外
		}else {
			queryBuilder.append(" AND id = ?").append(" OR status = ?").append(" OR classification = ?")
						.append(" OR task = ?").append(" OR description = ?").append(" OR creator = ?");
		}
		
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(queryBuilder.toString())){
			
			//検索ワードを各パラメータにセット(クエリ内のパラメータの数だけ繰り返し処理)
			if(isDateTime) {
				for(int i = 0; i < 8; i++) {
					ps.setObject(i + 1, keyWord);
				}
			}else {
				for(int i = 0; i < 6; i++) {
					ps.setObject(i + 1, keyWord);
				}
			}
			
			//DBからデータを取得
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					searchedResultTask.add(mapResultSetToTodoInfo(rs));
				}				
			}
						
			return searchedResultTask;

		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
	}
	
	
	
	//ゴミ箱内のタスク検索
	public static List<TodoInfo> getSearchedCashTask(String keyWord) throws ManageException{	
		//TODO 検索ワードが"時刻のとき"で分岐する必要がある
		List<TodoInfo> searchedResultTask = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder(TaskQueries.dynamicCashedQuery);
		
		//時刻かどうかを判定するフラグ
		boolean isDateTime = false;
		
		//検索ワードをformatterの形式に変換できれば、日付けを含んだクエリを生成する
		try {
			LocalDateTime.parse(keyWord, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			isDateTime = true;
		}catch(Exception e) {
			isDateTime = false;
		}
		
		//時刻が含まれている場合は、時刻を含んだクエリを生成する
		if(isDateTime) {
			queryBuilder.append(" AND id = ?").append(" OR status = ?").append(" OR classification = ?")
						.append(" OR task = ?").append(" OR description = ?").append(" OR createDateTime = ?")
						.append(" OR updateDateTime = ?").append(" OR creator = ?");
		//検索ワードが時刻出ない場合、時刻は検索対象外
		}else {
			queryBuilder.append(" AND id = ?").append(" OR status = ?").append(" OR classification = ?")
						.append(" OR task = ?").append(" OR description = ?").append(" OR creator = ?");
		}
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(queryBuilder.toString())){
			//検索ワードを各パラメータにセット(クエリ内のパラメータの数だけ繰り返し処理)
			if(isDateTime) {
				for(int i = 0; i < 8; i++) {
					ps.setObject(i + 1, keyWord);
				}
			}else {
				for(int i = 0; i < 6; i++) {
					ps.setObject(i + 1, keyWord);
				}
			}
			
			//DBからデータを取得
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					searchedResultTask.add(mapResultSetToTodoInfo(rs));
				}				
			}
			return searchedResultTask;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ManageException("EM003", e);
		}
	}
	
	
	
	//フィルターをかけて一覧取得
	public static List<TodoInfo> getFilteredTaskList(TodoInfo filteredTask) throws ManageException{
		
		//フィルタ―後のDBから取得したデータを格納するリスト
		List<TodoInfo> filteredTaskList = new ArrayList<>();
		//動的クエリの生成準備
		StringBuilder queryBuilder = new StringBuilder(TaskQueries.dynamicQuery);
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
		if(filteredTask.getIsFavorite()) {
			queryBuilder.append(" AND isFavorite = ?");
			parameters.add(filteredTask.getIsFavorite());
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
				dbData.isFavorite = rs.getBoolean("isFavorite");
			}catch(SQLException e){
				try {
					throw new ManageException("EM003", e);					
				}catch(ManageException e2) {
					throw new RuntimeException(e2.getMessageId());
				}
			}
		}).build();
	}
	
	
	//ページネーション処理で指定されたページ番号のデータを取得する
	public static List<TodoInfo> getSpecifyColumnTask(int pageNum, int solidTaskCount) throws ManageException{
		//そのページに表示する最初の行番号を取得
		int startPage = (pageNum - 1) * solidTaskCount;
		//ページごとに表示するタスクを格納したリスト
		List<TodoInfo> pageByPageTaskList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.getLimitOffsetQuery)){
			ps.setInt(1, startPage);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					pageByPageTaskList.add(mapResultSetToTodoInfo(rs));
				}				
			}
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
		return pageByPageTaskList;
	}
	
	
	//TODO ゴミ箱内でページングを行う処理=冗長なので、上記と組み合わせるかあとで検討
	public static List<TodoInfo> getSpecifyColumnCashTask(int pageNum, int solidTaskCount) throws ManageException{
		//そのページに表示する最初の行番号を取得
		int startPage = (pageNum - 1) * solidTaskCount;
		//ページごとに表示するタスクを格納したリスト
		List<TodoInfo> pageByPageTaskList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(TaskQueries.getCashLimitOffsetQuery)){
			ps.setInt(1, startPage);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					pageByPageTaskList.add(mapResultSetToTodoInfo(rs));
				}				
			}
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
		return pageByPageTaskList;
	}
	
	
	
	//todoList.jspでの並べかえ処理
	public static List<TodoInfo> getListOrderByParameters(String tHeaderParameter, int any_pushedCounta) throws ManageException{
		//並べ替え後のデータを保持するリスト
		List<TodoInfo> orderedTaskList = new ArrayList<TodoInfo>();
		try(Connection con = dbc.getConnection()){
			String queryBuilder;
			//偶数回の場合は昇順、奇数回の場合は降順
			if(any_pushedCounta % 2 == 0) {
				queryBuilder = "SELECT * FROM todolist ORDER BY " + tHeaderParameter + " ASC";
			}else {
				queryBuilder = "SELECT * FROM todolist ORDER BY " + tHeaderParameter + " DESC";
			}
			buildQuery(queryBuilder, con, orderedTaskList);
			return orderedTaskList;
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//TODO todoCashList.jspでの並べかえ処理＝冗長なので、上記と組み合わせる必要あるか検討
	public static List<TodoInfo> getCashListOrderByParameters(String tHeaderParameter, int any_pushedCounta) throws ManageException{
		//並べ替え後のデータを保持するリスト
		List<TodoInfo> orderedTaskList = new ArrayList<TodoInfo>();
		try(Connection con = dbc.getConnection()){
			String queryBuilder;
			//偶数回の場合は昇順、奇数回の場合は降順
			if(any_pushedCounta % 2 == 0) {
				queryBuilder = "SELECT * FROM todocashlist ORDER BY " + tHeaderParameter + " ASC";
			}else {
				queryBuilder = "SELECT * FROM todocashlist ORDER BY " + tHeaderParameter + " DESC";
			}
			buildQuery(queryBuilder, con, orderedTaskList);
			return orderedTaskList;
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//並べかえの結果を返す
	private static void buildQuery(String queryBuilder, Connection con, List<TodoInfo> orderedTaskList) throws ManageException{
		try(PreparedStatement ps = con.prepareStatement(queryBuilder);
			ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				orderedTaskList.add(mapResultSetToTodoInfo(rs));
			}
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
}
