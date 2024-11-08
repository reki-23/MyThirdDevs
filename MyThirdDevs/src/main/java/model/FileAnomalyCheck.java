package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import exception.FileOverSizeException;
import exception.InappropriateFileException;
import exception.ManageException;
import exception.NoElementsOnFileException;

/*
 *読み込むファイルに異常がないを検査するクラス 
 * 
 * ■検査項目
 * 　・ファイルの存在確認
 *  ・ファイルのサイズ0より大きく上限以下か
 *  ・CSV限定
 *  ・ファイル内にデータが存在しているかどうか
 *  ・ファイル内にヘッダーが存在しているかどうか
 */

public class FileAnomalyCheck {
	
	/*
	 * 1MB = 1024B * 1024B
	 * 最大50MBまで読みこめるようにする
	 */
	private static final int FILE_MAX_SIZE = 52428800;
	private static final String header = "No,ステータス,分類,タスク名,タスク概要,作成者";
	
	public void anomalyCheck(String fileName, Path importedFile) throws ManageException{
		
		try {
			//ファイルの存在確認
			if(!Files.exists(importedFile)) {
				throw new ManageException("EM011", new FileNotFoundException());
			}
			
			//CSVファイルかどうかを確認
			if(!fileName.endsWith(".csv")) {
				throw new ManageException("EM013", new InappropriateFileException());
			}
			
			//ファイルのサイズが0かどうかを確認
			if(Files.size(importedFile) == 0) {
				throw new ManageException("EM014", new NoElementsOnFileException());
			}
			
			//ファイルのサイズが最大サイズを超えていないかを確認
			if(Files.size(importedFile) > FILE_MAX_SIZE) {
				throw new ManageException("EM012", new FileOverSizeException());
			}
			
			
			try(BufferedReader br = Files.newBufferedReader(importedFile)){
				String firstRow = br.readLine(); //1行目
				String secondRow = br.readLine(); //2行目
				//ヘッダーしか存在していない場合
				if(secondRow == null) {
					throw new ManageException("EM010", new NoElementsOnFileException());
				}
				
				//ヘッダーが適切ではない場合
				if(!firstRow.equals(header)) {
					throw new ManageException("EM014", new InappropriateFileException());
				}
			}
		
		}catch(IOException e) {
			throw new ManageException("EM002", e);
		}
	}
}