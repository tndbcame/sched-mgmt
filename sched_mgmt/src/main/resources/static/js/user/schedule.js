/********************************
 * 選択された年月ごとに日付を生成
 ********************************/
let year = document.querySelector("#year");
let month = document.querySelector("#month");
let day = document.querySelector("#day");
let lastDayOfTheMonth = 31;
/**
 * selectのoptionタグを生成する
 */
function createDayOptions(elem, val) {
	let option = document.createElement("option");
	option.text = val;
	option.value = val;
	elem.appendChild(option);
}
/**
 * 日付を変更する
 */
function chengeTheDay() {
	//日付を削除
	day.innerHTML = '';

	//選択された年月の最終日を計算
	lastDayOfTheMonth = new Date(year.value, month.value, 0).getDate();

	//選択された年月の日付を生成
	for (var i = 1; i <= lastDayOfTheMonth; i++) {
		createDayOptions(day, i);
	}
}
year.addEventListener('change', function() {
	chengeTheDay();
});
month.addEventListener('change', function() {
	chengeTheDay();
});


/******************************************
 * スケジュールを非同期通信でリクエスト送信
 ******************************************/

// メタタグに埋め込んだ情報を取得する
let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

// Ajax通信時に、リクエストヘッダにトークンを埋め込むよう設定
$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});

/**
 * スケジュールを登録する
 */
$(function() {
	// 登録ボタンを押したときの処理
	$('#insertSchedule').on('click', function() {

		// リクエスト設定
		var request = {
			userId: document.getElementById('user_id').value,
			accountName: document.getElementById('account_name').value,
			year: document.getElementById('year').value,
			month: document.getElementById('month').value,
			day: document.getElementById('day').value,
			schedule: document.getElementById('scheduleContent').value,
		};
		$.ajax({
			type: 'post',
			url: '/user/schedule/register',
			data: JSON.stringify(request),
			contentType: 'application/json',
			dataType: "json",
			cache: false,
			success: function(data) {
				let errorMsg =
					document.getElementById('errorMsg');
				errorMsg.textContent = '';
				if (!data["errorMsg"]) {
					// 通信が成功した場合に受け取る
					schedule = data["schedule"];
					day = data["day"];
					// スケージュールの空白を削除
					let _schedule =
						document.getElementById(day).textContent.replace(/\s+/g, '');
					// 空文字かどうかを判定する
					if (_schedule.length != 0) {
						_schedule += "," + schedule;
					} else {
						_schedule += schedule;
					}
					// スケジュールを再度設定
					document.getElementById(day).textContent = _schedule;
				} else {
					errorMsg.textContent = data["errorMsg"];
				}
			},
		});
	});
});

/**
 * スケジュールを表示する
 */
$(function() {
	// 登録ボタンを押したときの処理
	$('#displaySchedule').on('click', function() {

		// リクエスト設定
		var request = {
			userId: document.getElementById('user_id').value,
			year: document.getElementById('year').value,
			month: document.getElementById('month').value,
			day: document.getElementById('day').value,
		};
		$.ajax({
			type: 'post',
			url: '/user/schedule/disp',
			data: JSON.stringify(request),
			contentType: 'application/json',
			dataType: "json",
			cache: false,
			success: function(data) {
				createTbl(data);
			},
		});
	});
});

/**
 * 日付tableを再作成する
 */
function createTbl(data) {
	// テーブルを削除
	let table = document.getElementById("tbl");
	table.innerHTML = "";
	// テーブルを作成する
	for (var i = 1; i <= lastDayOfTheMonth; i++) {
		let tbody = document.createElement("tbody");
		let tr = document.createElement("tr");
		let td1 = document.createElement("td");
		let td2 = document.createElement("td");
		// 予定欄のidを設定
		td2.setAttribute('id', i);
		// 日付を設定
		td1.textContent = i;
		// 該当日付にデータが存在する場合はスケジュール内容を格納する
		if (i.toString() in data) {
			td2.innerHTML = data[i.toString()];
		}
		tr.appendChild(td1);
		tr.appendChild(td2);
		tbody.appendChild(tr);
		table.appendChild(tbody);
	}
}