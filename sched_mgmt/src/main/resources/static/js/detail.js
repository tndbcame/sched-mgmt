/**********************************************
 * ユーザー詳細情報を非同期通信でリクエスト送信
 **********************************************/

// メタタグに埋め込んだ情報を取得する
let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

// Ajax通信時に、リクエストヘッダにトークンを埋め込むよう設定
$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});

/**
 * 削除処理時にユーザー情報を取得してアラートする
 */
$(function() {
	// 削除ボタンを押したときの処理
	$('#deleteBtn').on('click', function() {

		// リクエスト設定
		var request = {
			userIdStr: document.getElementById('userId').value,
		};
		$.ajax({
			type: 'post',
			url: '/alertUserDetailForDelete',
			data: JSON.stringify(request),
			contentType: 'application/json',
			dataType: "json",
			cache: false,
			success: function(data) {
				let errorMsg =
					document.getElementById('errorMsg');
				errorMsg.textContent = '';
				if (!data['errorMsg']) {
					if (confirm("ユーザーID："
						+ data['userId']
						+ ", アカウント名："
						+ data['accountName']
						+ ", ユーザー名："
						+ data['userName']
						+ "\n"
						+ "こちらを削除してもよろしいでしょうか？")) {
						document.form.action = "/deleteUser";
						document.form.submit();
					} else {
						alert("キャンセルされました");
					}
				} else {
					errorMsg.textContent = data["errorMsg"];
				}
			},
		});
	});
});
