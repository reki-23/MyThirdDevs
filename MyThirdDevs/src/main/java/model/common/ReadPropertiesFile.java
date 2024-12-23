package model.common;

/*
 * 
 * プロパティファイルを読み込むクラス
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.exception.ManageException;
import model.exception.PropertiesFileNotFoundException;

public class ReadPropertiesFile {
	public void getPropertiesFileInfo(HttpServletRequest request, HttpServletResponse response, Properties prop) throws ServletException, IOException{
		try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
			if(input == null) {
				//読み込み失敗時
				throw new ManageException("EM001", new PropertiesFileNotFoundException());
			}
			try(InputStreamReader reader = new InputStreamReader(input, "UTF-8")){
				prop.load(reader);
			}
		}catch(ManageException e) {
			
		}
	}
}
