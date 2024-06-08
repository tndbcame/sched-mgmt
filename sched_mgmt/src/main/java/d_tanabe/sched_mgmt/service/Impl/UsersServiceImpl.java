package d_tanabe.sched_mgmt.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.repository.UsersMapper;
import d_tanabe.sched_mgmt.service.UsersService;


/**
 * サービスクラスの実行クラス sqlのメソッドが呼ばれる
 */
@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	public UsersMapper usersMapper;
	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * ユーザーを取得する
	 */
	@Override
	public List<Users> selectByUser(
		Users users,
		Integer perPage,
		Integer startIndex) {

		List<Users> userList = new ArrayList<>();
		List<Users> tmpUserList = usersMapper.selectByUser(
			users.getStatus(),
			users.getId(),
			users.getAccountName(),
			users.getUserName(), 
			perPage,
			startIndex);
		
		// 画面表示用に設定する
		for (Users user : tmpUserList) {
			if ("1".equals(user.getStatus())) {
				user.setStatus("有効");
			} else if ("2".equals(user.getStatus())) {
				user.setStatus("無効");
			}
			if ("1".equals(user.getRole())) {
				user.setRole("管理者");
			} else if ("2".equals(user.getRole())) {
				user.setRole("一般");
			}

			userList.add(user);
		}

		return userList;
	}

	/**
	 * ユーザーIdからユーザー情報を検索する
	 */
	@Override
	public Users selectByUserId(Integer userId) {
		return usersMapper.selectByUserId(userId);
	}

	/**
	 * ユーザー存在チェック
	 */
	@Override
	public boolean existsUser(Integer userId, String password) {

		// ユーザー情報の存在チェック
		String dbPass = usersMapper.selectPassByUserId(userId);

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
	public void signupUser(Users users, boolean admin) {

		// パスワードのハッシュ化
		String hash = passwordEncoder.encode(users.getPassword());
		users.setPassword(hash);

		// 権限を設定
		String role = "2";
		if (admin) {
			role = "1";
		}
		users.setRole(role);

		// ステータスは有効で登録
		users.setStatus("1");

		usersMapper.signupUser(users);
	}

	/**
	 * ユーザーを更新する
	 */
	@Override
	public void updateUser(
	Users users,
	boolean adminFlg,
	boolean statusFlg) {

		// 権限を設定
		String role = "2";
		if (adminFlg) {
			role = "1";
		}
		users.setRole(role);

		// 状態を設定
		String status = "1";
		if (statusFlg) {
			status = "2";
		}
		users.setStatus(status);

		usersMapper.updateUser(users);
	}

	/**
	 * パスワードを更新する
	 */
	@Override
	public void updatePassword(Integer userId, String password) {
		String hash = passwordEncoder.encode(password);
		usersMapper.updatePassword(userId, hash);
	}

	/**
	 * ユーザーを削除する
	 */
	@Override
	public void deleteUser(Integer userId) {
		usersMapper.deleteUser(userId);
	}
}
