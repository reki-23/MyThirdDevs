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

//キャンセルボタンを押下したらタスクフィルターモーダルを非表示にする
function hiddenFilterModalOnCancel(){
	document.querySelector("#export-csv-filter").style.visibility = 'hidden';
}


//お気に入りのカバブアイコンを押下したときのモーダル表示
document.addEventListener('DOMContentLoaded', () => {
  const filtering_favorite_icon = document.querySelector('#filtering-favorite'); // カバブアイコン
  const filtering_favoriteTask_modal = document.querySelector('#filtering-favoriteTask-modal');
  const filtering_favoriteTask_modal_mask = document.querySelector('#filtering-favoriteTask-modal-mask');

  filtering_favorite_icon.addEventListener('click', () => {
    filtering_favoriteTask_modal.style.display = 'block';
    filtering_favoriteTask_modal.style.opacity = '1';
    filtering_favoriteTask_modal.style.visibility = 'visible';

    filtering_favoriteTask_modal_mask.style.display = 'block';
    filtering_favoriteTask_modal_mask.style.opacity = '1';
    filtering_favoriteTask_modal_mask.style.visibility = 'visible';
  });
});

