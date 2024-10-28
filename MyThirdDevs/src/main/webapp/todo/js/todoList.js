/*
function setFormAction(){
	var form = document.getElementById('#register_newTask');
	//var register_cancel = document.getElementById('#register_cancel');
	//var register_submit = document.getElementById('#register_submit');
	var clickButton = document.activeElement.name;
	
	if(clickButton == 'register_submit'){
		//タスク登録ボタンを押下した場合
		alert("おい");
		form.action = '${pageContext.request.contextPath}/todo/TodoRegisterServlet';
	}else if(clickButton == 'register_cancel'){
		//キャンセルボタンを押下した場合＝一覧取得サーブレットに遷移してJSPへフォワードの流れ
		form.action = 'todoList.jsp';
	}
}
*/

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