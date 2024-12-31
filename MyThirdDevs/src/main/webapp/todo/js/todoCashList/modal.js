//選択したタスクを復元するボタンを押下した際のモーダル表示制御
document.addEventListener('DOMContentLoaded', () => {
  const restore_button = document.querySelector('#restore-button');
  const restore_confirm_modal = document.querySelector('#restore_confirm_modal');
  const res_mask = document.querySelector('#res-mask');
  

  restore_button.addEventListener('click', () => {
    restore_confirm_modal.style.display = 'block';
    restore_confirm_modal.style.opacity = '1';
    restore_confirm_modal.style.visibility = 'visible';

    res_mask.style.display = 'block';
    res_mask.style.opacity = '1';
    res_mask.style.visibility = 'visible';
  });
});


//復元確認モーダルを閉じる
function closeRestoreConfirmModal(){
	document.querySelector("#restore_confirm_modal").style.visibility = "hidden";
}

