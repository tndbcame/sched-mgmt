package d_tanabe.sched_mgmt.validation;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidation {

	//メッセージ
	@Autowired
	MessageSource messagesource;

	/**
	 * 新しいパスワードと確認用パスワードが同一かチェックしてエラーメッセージを返却
	 * @param pass1
	 * @param pass2
	 * @return msg(エラーメッセージ)
	 */
	public String getPasswordErrorMsg(String pass1, String pass2) {
		String msg = "";
		if (!pass1.equals(pass2)) {
			//メッセージを取得する
			msg = messagesource.getMessage("003.validation.notNewPasswords", null, Locale.JAPAN);
		}
		return msg;

	}

}
