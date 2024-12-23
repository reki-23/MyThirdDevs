package model.dao;


/*
 * DAOクラスで使用するクエリを一元管理するクラス
 */
public class TaskQueries {
	
	public TaskQueries() {
		//外部からインスタンス化できないようにする
	}
	static final String insertSql = "INSERT INTO todolist VALUES(?,?,?,?,?,?,?,?,?)";
	static final String insertCashSql = "INSERT INTO todocashlist SELECT * FROM todolist WHERE id = ?";
	static final String insertAllCashSql = "INSERT INTO todocashlist SELECT * FROM todolist";
	static final String selectIdSql = "SELECT * FROM todolist WHERE id = ?";
	static final String selectFavIdSql = "SELECT isFavorite FROM todolist WHERE id = ?";
	static final String selectFavSql = "SELECT * FROM todolist WHERE isFavorite = 1";
	static final String deleteSql = "DELETE FROM todolist";
	static final String deleteCashSql = "DELETE FROM todocashlist";
	static final String getSql = "SELECT * FROM todolist";
	static final String getCashSql = "SELECT * FROM todocashlist";
	static final String updateFavFlgSql = "UPDATE todolist SET isFavorite = ? WHERE id = ?";
	static final String individualDeleteSql = "DELETE FROM todolist WHERE id = ?";
	static final String getLimitOffsetQuery = "SELECT * FROM todolist LIMIT 50 OFFSET ?";
	static final String getCashLimitOffsetQuery = "SELECT * FROM todocashlist LIMIT 50 OFFSET ?";
	static String dynamicQuery = "SELECT * FROM todolist WHERE 1=1";
}
