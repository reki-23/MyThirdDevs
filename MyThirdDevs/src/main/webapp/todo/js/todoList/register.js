//選択したファイル名をid="fileName"のvalue属性にセット
function validateFileUpload(){
	var fileInput = document.getElementById("csvFile");
	if(fileInput.files.length > 0){
		var fileName = fileInput.files[0].name;
		//ファイルが選択されていればファイル名をvalueに渡す
		document.getElementById("fileName").value = fileName;
	}else{
		//ファイルが選択されていない場合空文字をvalueに渡す
		document.getElementById("fileName").value = "";		
	}
}


//一括登録のボタンの活性・非活性を操作
function validateForm(){
	var fileInput = document.getElementById("csvFile");
	if(fileInput.files.length > 0){
		//ファイル選択時、一括登録ボタンを活性
		document.getElementById("bulk_register").disabled = false;
	}else{
		//ファイル未選択時、一括登録ボタンを非活性
		document.getElementById("bulk_register").disabled = true; 
	}
}