package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import exception.ManageException;
import exception.PropertiesFileNotFoundException;

class DBConnection {
	public Connection getConnection() throws ManageException{
		
		Properties prop = new Properties();
		try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
			if(input == null) {
				//プロパティファイル読み込み失敗時
				throw new PropertiesFileNotFoundException();
			}
			prop.load(input);
			
			final String URL = prop.getProperty("db.url");
			final String USER = prop.getProperty("db.username");
			final String PASS = prop.getProperty("db.password");
			
			return DriverManager.getConnection(URL, USER, PASS);
			
		}catch(PropertiesFileNotFoundException e_prop) {
			//共通例外クラス
			throw new ManageException("EM001", e_prop);
		}catch(IOException e_io) {
			//共通例外クラス
			throw new ManageException("EM002", e_io);
		}catch(SQLException e_con) {
			//共通例外クラス
			throw new ManageException("EM003", e_con);
		}
	}
}
