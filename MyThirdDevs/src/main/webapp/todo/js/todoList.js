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



//タスク登録モーダル
document.addEventListener('DOMContentLoaded', () => {
  const register_button = document.querySelector('#register-button');
  const register_cancel = document.querySelector('#register_cancel');
  const register_task = document.querySelector('#register_task');
  const register_mask = document.querySelector('#register_mask');

  const showKeyframes = {
    opacity: [0, 1],
    visibility: 'visible',
  };
  const hideKeyframes = {
    opacity: [1, 0],
    visibility: 'hidden',
  };
  const options = {
    duration: 200,
    easing: 'ease',
    fill: 'forwards',
  };

  // モーダルウィンドウを開く
  register_button.addEventListener('click', () => {
    register_task.animate(showKeyframes, options);
    register_mask.animate(showKeyframes, options);
  });

  // モーダルウィンドウを閉じる
  register_cancel.addEventListener('click', () => {
    register_task.animate(hideKeyframes, options);
    register_mask.animate(hideKeyframes, options);
  });
});



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
		document.getElementById("indi_submit").disabled = true;
	}else{
		document.getElementById("indi_submit").disabled = false;
	}
}