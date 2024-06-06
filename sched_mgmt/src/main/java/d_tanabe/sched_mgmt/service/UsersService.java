package d_tanabe.sched_mgmt.service;

import java.util.List;

import d_tanabe.sched_mgmt.model.Users;


public interface UsersService {

	/**
	 * ユーザー情報を元に検索する
	 * @param users
	 * @param perPage
	 * @param startIndex
	 * @return List(ユーザー情報)
	 */
	public List<Users> selectByUser(Users users, Integer perPage, Integer startIndex);

	/**
	 * IDからユーザー情報を取得
	 * @param userId
	 * @return Users
	 */
	public Users selectByUserId(Integer userId);
	
	/**
	 * ユーザー存在チェック
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean existsUser(Integer userId, String password);

	/**
	 * ユーザー登録
	 * @param users
	 * @param admin
	 */
	public void signupUser(Users users, boolean admin);

	/**
	 * ユーザー情報を更新する
	 * @param users
	 * @param admin
	 * @param status
	 */
	public void updateUser(Users users, boolean admin, boolean status);

	/**
	 * パスワードを更新する
	 * @param userId
	 * @param password
	 */
	public void updatePassword(Integer userId, String password);
	
	/**
	 * ユーザーを削除する
	 * @param userId
	 */
	public void deleteUser(Integer userId);

}
