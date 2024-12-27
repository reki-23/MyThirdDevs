/*共通処理 */

//設定ボタン押下後のコンテクストメニュー表示制御
function submitSetting(){
	document.querySelector('#context-menu').style.visibility = 'visible';
}

//コンテキストメニューで閉じるを押下した場合、コンテキストメニューを非表示にする
function submitHiddenContextMenu(){
	document.querySelector('#context-menu').style.visibility = 'hidden';
}