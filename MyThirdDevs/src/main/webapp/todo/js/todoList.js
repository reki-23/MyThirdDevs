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



//タスクフィルターモーダル
document.addEventListener('DOMContentLoaded', () => {
  const export_filter_csv_button = document.querySelector('#export-filter-csv-button');
  const filter_cancel = document.querySelector('#filter_cancel');
  const export_csv_filter = document.querySelector('#export-csv-filter');
  const export_csv_filter_mask = document.querySelector('#export-csv-filter-mask');

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
  export_filter_csv_button.addEventListener('click', () => {
    export_csv_filter.animate(showKeyframes, options);
    export_csv_filter_mask.animate(showKeyframes, options);
  });

  // モーダルウィンドウを閉じる
  filter_cancel.addEventListener('click', () => {
    export_csv_filter.animate(hideKeyframes, options);
    export_csv_filter_mask.animate(hideKeyframes, options);
  });
});


//一括削除か個別削除かを共通の削除確認モーダルのhiddenに設定し、モーダルを表示する
function setDeleteTypeAndModalDisp(type){
	document.getElementById('deleteType').value = type;
	const bulk_submit = document.querySelector('#bulk_submit');
	const indi_submit = document.querySelector('#indi_submit');
	const delete_confirm_modal = document.querySelector('#delete_confirm_modal');
	const del_mask = document.querySelector('#del_mask');
	
	const showKeyframes = {
	    opacity: [0, 1],
	    visibility: 'visible',
	  };
	  
	  const options = {
	    duration: 100,
	    easing: 'ease',
	    fill: 'forwards',
	  };
	
	  // モーダルウィンドウを開く
	  bulk_submit.addEventListener('click', () => {
	    delete_confirm_modal.animate(showKeyframes, options);
	    del_mask.animate(showKeyframes, options);
	  });
	  
	  // モーダルウィンドウを開く
	  indi_submit.addEventListener('click', () => {
	    delete_confirm_modal.animate(showKeyframes, options);
	    del_mask.animate(showKeyframes, options);
	  });
}


//削除確認モーダルでキャンセルを押下した場合、モーダルを非表示にする
function closeModal(){
	const delete_confirm_modal = document.querySelector('#delete_confirm_modal');
	const del_mask = document.querySelector('#del_mask');

	const hideKeyframes = {
	    opacity: [1, 0],
	    visibility: 'hidden',
 	};

	const options = {
	   	duration: 100,
	    easing: 'ease',
	   	fill: 'forwards',
	};

	// モーダルウィンドウを非表示にする
	delete_confirm_modal.animate(hideKeyframes, options);
	del_mask.animate(hideKeyframes, options);
}


//CSV出力ボタンを押下したらタスクフィルターモーダルを非表示にする
function hiddenFilterModal(){
	const export_csv_filter = document.querySelector('#export-csv-filter');
	const export_csv_filter_mask = document.querySelector('#export-csv-filter-mask');

  	const hideKeyframes = {
    	opacity: [1, 0],
    	visibility: 'hidden',
  	};

  	const options = {
    	duration: 100,
    	easing: 'ease',
   		fill: 'forwards',
  	};

  	// モーダルウィンドウを非表示にする
	export_csv_filter.animate(hideKeyframes, options);
	export_csv_filter_mask.animate(hideKeyframes, options);
 	
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
		document.getElementById("indi_submit").disabled = true;
	}else{
		document.getElementById("indi_submit").disabled = false;
	}
}


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
function submitFilteringFavoriteTask(checkbox){
	if(checkbox.checked){
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


//お気に入りのカバブアイコンを押下したときのモーダル表示
document.addEventListener('DOMContentLoaded', () => {
  const filtering_favorite = document.querySelector('#filtering-favorite');
  const filtering_favoriteTask_modal = document.querySelector('#filtering-favoriteTask-modal');
  const filtering_favoriteTask_modal_mask = document.querySelector('#filtering-favoriteTask-modal-mask');

  const showKeyframes = {
    opacity: [0, 1],
    visibility: 'visible',
  };
  const options = {
    duration: 200,
    easing: 'ease',
    fill: 'forwards',
  };

  // モーダルウィンドウを開く
  filtering_favorite.addEventListener('click', () => {
    filtering_favoriteTask_modal.animate(showKeyframes, options);
    filtering_favoriteTask_modal_mask.animate(showKeyframes, options);
  });
});

