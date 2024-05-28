package d_tanabe.sched_mgmt.controller.user.edit;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import d_tanabe.sched_mgmt.form.user.edit.EditForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.UsersService;


/**
 * Restユーザー詳細画面コントローラー(非同期通信用の処理)
 */
@RestController
public class EditRestController {

	//サービスクラス
	@Autowired
	private UsersService usersService;

	//メッセージ
	@Autowired
	MessageSource messagesource;

	/**
	 * ユーザーを削除する際にアラートを表示する
	 * @param form (詳細画面からの入力をバインド)
	 * @return UserDetailForm ユーザー情報をセットして返却
	 */
	@PostMapping("/alertUserDetailForDelete")
	public EditForm alertUserDetailForDelete(@RequestBody EditForm form) {

		try {
			//ユーザー情報を取得
			Users users = 
					usersService.findByUserId(Integer.parseInt(form.getUserIdStr()));

			//ユーザー情報をセットする
			form.setUserId(users.getId());
			form.setAccountName(users.getAccountName());
			form.setUserName(users.getUserName());
		} catch (Exception e) {

			//エラーメッセージを格納
			String errorMsg = messagesource.getMessage(
					"004.validation.deleteFailed", null, Locale.JAPAN);
			form.setErrorMsg(errorMsg);
		}
		return form;
	}
}
