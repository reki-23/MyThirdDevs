/*共通処理 */

//設定ボタン押下後のコンテクストメニュー表示制御
function submitSetting(){
	document.querySelector('#context-menu').style.visibility = 'visible';
}

//コンテキストメニューで閉じるを押下した場合、コンテキストメニューを非表示にする
function submitHiddenContextMenu(){
	document.querySelector('#context-menu').style.visibility = 'hidden';
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