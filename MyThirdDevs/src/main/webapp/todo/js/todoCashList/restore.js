/*タスク復元時のチェックボックス制御*/
let seletctedTaskIds = [];
//チェックボックスの状態を切り替え
function submitFormOnCheck(checkbox){
	const taskId = checkbox.value;
	//チェック時はタスクidを追加し、チェックを外した場合はidを削除
	if(checkbox.checked){
		seletctedTaskIds.push(taskId);
	}else{
		seletctedTaskIds = seletctedTaskIds.filter(id => id !== taskId);
	}
	//hiddenに選択されたタスクのidをセット
	document.getElementById("selectedRestoreIds").value = seletctedTaskIds;
	
	//1つでもチェックボックスがチェックされていたら可視性をON
	if(seletctedTaskIds.length == 0){
		document.querySelector("#restore-button").style.visibility = 'hidden';
	}else{
		document.querySelector("#restore-button").style.visibility = 'visible';	
	}
}