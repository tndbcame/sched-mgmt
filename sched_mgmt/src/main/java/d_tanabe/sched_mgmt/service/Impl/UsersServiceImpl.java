package d_tanabe.sched_mgmt.service.Impl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.repository.UsersMapper;
import d_tanabe.sched_mgmt.service.UsersService;


/**
 * サービスクラスの実行クラス
 * sqlのメソッドが呼ばれる
 */
@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	public UsersMapper usersMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	//メッセージ
	@Autowired
	MessageSource messagesource;

	/**
	 * ユーザーを取得する
	 */
	@Override
	public List<Users> selectByUser(Users users, Integer perPage, Integer startIndex) {
		return usersMapper.selectByUser(users.getStatus(),
				users.getId(),
				users.getAccountName(),
				users.getUserName(),
				perPage,
				startIndex);
	}

	/**
	 * ユーザーIdからユーザー情報を検索する
	 */
	@Override
	public Users findByUserId(Integer userId) {
		return usersMapper.findByUserId(userId);
	}

	/**
	 * ユーザー存在チェック
	 */
	@Override
	public boolean existsUser(Integer userId, String password) {

		//ユーザー情報の存在チェック
		String dbPass = usersMapper.findPassByUserId(userId);

		if (passwordEncoder.matches(password, dbPass)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ユーザーを登録する
	 */
	@Override
	public void signUpUser(Users users, boolean admin) {

		//パスワードのハッシュ化
		String hash = passwordEncoder.encode(users.getPassword());
		users.setPassword(hash);

		//権限を設定
		String role = "USER";
		if (admin) {
			role = "ADMIN";
		}
		users.setRole(role);

		//ステータスは有効で登録
		users.setStatus("VALID");

		usersMapper.signUpUser(users);
	}

	/**
	 * ユーザーを更新する
	 */
	@Override
	public void upDateUser(Users users, boolean adminFlg, boolean statusFlg) {

		//権限を設定
		String role = "USER";
		if (adminFlg) {
			role = "ADMIN";
		}
		users.setRole(role);

		//状態を設定
		String status = "VALID";
		if (statusFlg) {
			status = "INVALID";
		}
		users.setStatus(status);

		usersMapper.upDateUser(users);
	}

	/**
	 * パスワードを更新する
	 */
	@Override
	public void upDatePassword(Integer userId, String password) {
		String hash = passwordEncoder.encode(password);
		usersMapper.upDatePassword(userId, hash);
	}
	
	/**
	 * ユーザーを削除する
	 */
	@Override
	public void deleteUser(Integer userId) {
		usersMapper.deleteUser(userId);
	}

	/**
	 * メッセージを取得する
	 */
	@Override
	public String getCompleteMessage(String completeMessageFlg) {

		String message = null;
		//messages.propertiesからメッセージを取得する
		if ("1".equals(completeMessageFlg)) {
			message = messagesource.getMessage("completeMessage", new String[] { "登録" }, Locale.JAPAN);
		} else if ("2".equals(completeMessageFlg)) {
			message = messagesource.getMessage("completeMessage", new String[] { "更新" }, Locale.JAPAN);
		} else if ("3".equals(completeMessageFlg)) {
			message = messagesource.getMessage("completeMessage", new String[] { "削除" }, Locale.JAPAN);
		} else if ("4".equals(completeMessageFlg)) {
			message = messagesource.getMessage("completeMessage", new String[] { "パスワードの更新" }, Locale.JAPAN);
		}
		return message;

	}
}
