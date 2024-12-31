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


//タスク個別削除時のチェックボックスの制御
let selectedTaskIds = [];
//チェックボックスの状態を切り替え
function submitFormOnCheck(checkbox){
	const taskId = checkbox.value;
	//チェックされたとき、そのタスクNoを追加し、個別削除ボタンを活性化
	if(checkbox.checked){
		selectedTaskIds.push(taskId);
	}else{
		//チェックが外れた場合はidを削除
		selectedTaskIds = selectedTaskIds.filter(id => id !== taskId);
	}
	//hiddenに選択されたタスクidのリストをセット
	document.getElementById("selectedIds").value = selectedTaskIds;
	
	//チェックされてれば個別削除ボタンを活性
	if(selectedTaskIds.length == 0){
		document.querySelector('.delete-individual-list').style.visibility = 'hidden';
	}else{
		document.querySelector('.delete-individual-list').style.visibility = 'visible';
	}
}