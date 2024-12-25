/* タスクお気に入り登録時 */
let selectedTaskIdsOfFavorite = 0;
//チェックボックスの状態を切り替え
function submitFormOnCheckOfFavorite(checkbox){
	const taskId = checkbox.value;
	//チェックされたとき、そのタスクNoを保持
	if(checkbox.checked){
		selectedTaskIdsOfFavorite = taskId;
		
	}else{
		selectedTaskIdsOfFavorite = taskId;
	}
	//hiddenに選択されたタスクNoをvalueにセット
	document.getElementById("selectedFavIds").value = selectedTaskIdsOfFavorite;
	document.getElementById("selectedFavIds").form.submit();
}


/*お気に入りタスクを検索する際のチェックボックスを押下した際に即座にサーブレットに送信する処理*/
function submitFilteringFavoriteTask(value){
	if(value=="フィルタをかける"){
		document.getElementById("filteringFavorite").value = true;
	}else{
		document.getElementById("filteringFavorite").value = false;
	}
	document.getElementById("filteringFavorite").form.submit();
	
}


//画面読み込み時にお気に入りタスクのidをJSON形式で取得
document.addEventListener("DOMContentLoaded", function(){
	const favoriteTaskIdListJson = document.getElementById('favoriteTaskData');
		if(favoriteTaskIdListJson){
			const favoriteTaskIdList = JSON.parse(favoriteTaskIdListJson.dataset.tasks);
			//上記で取得したidに対して、CSSを適用する
			favoriteTaskIdList.forEach(taskId => {
				const checkbox = document.querySelector(`.favorite-checkbox[data-task-id="${taskId}"]`);
				if(checkbox){
					checkbox.classList.add('favorite');
				}
			});
		}	
});