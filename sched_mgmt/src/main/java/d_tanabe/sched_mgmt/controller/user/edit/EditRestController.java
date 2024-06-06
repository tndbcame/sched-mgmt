package d_tanabe.sched_mgmt.controller.user.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import d_tanabe.sched_mgmt.form.user.edit.EditForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.util.message.MessageManager;


/**
 * Restユーザー編集画面コントローラー(非同期通信用の処理)
 */
@RestController
public class EditRestController {
	
	@Autowired
	private UsersService usersService;
	@Autowired
	MessageSource messagesource;

	/**
	 * ユーザーを削除する際にアラートを表示する
	 * 
	 * @param form (編集画面からの入力をバインド)
	 * @return EditForm ユーザー情報をセットして返却
	 */
	@PostMapping("/user/edit/delete/alert")
	public EditForm deleteAlert(@RequestBody EditForm form) {

		try {
			// ユーザー情報を取得
			Users users = usersService.selectByUserId(Integer.parseInt(form.getUserIdStr()));

			// ユーザー情報を設定
			form.setUserId(users.getId());
			form.setAccountName(users.getAccountName());
			form.setUserName(users.getUserName());
		} catch (Exception e) {

			// エラーメッセージを設定
			form.setErrorMsg(MessageManager.DELETE_FAIRED.getMessage(messagesource));
		}
		return form;
	}
}
