/*お気に入りタスクを検索する際のチェックボックスを押下した際に即座にサーブレットに送信する処理*/
function submitFilteringFavoriteTask(value){
	if(value=="フィルタをかける"){
		document.getElementById("filteringFavorite").value = true;
	}else{
		document.getElementById("filteringFavorite").value = false;
	}
	document.getElementById("filteringFavorite").form.submit();
	
}
