package d_tanabe.sched_mgmt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import d_tanabe.sched_mgmt.model.Users;


/**
 * ユーザーTBLマッパー
 */
@Mapper
public interface UsersMapper {

	/**
	 * ユーザー情報を元に検索する
	 * @param status
	 * @param userId
	 * @param accountName
	 * @param userName
	 * @param perPage
	 * @param currentPage
	 * @return List(ユーザー情報)
	 */
	public List<Users> selectByUser(@Param("status") String status,
			@Param("userId") Integer userId,
			@Param("accountName") String accountName,
			@Param("userName") String userName,
			@Param("perPage") Integer perPage,
			@Param("currentPage") Integer currentPage);

	
	/**
	 * アカウント名からユーザー情報を取得
	 * @param accountName
	 * @return Users
	 */
	public Users findByAccountName(@Param("accountName") String accountName);

	/**
	 * ユーザーIdからユーザー情報を取得
	 * @param userId
	 * @return Users
	 */
	public Users findByUserId(@Param("userId") Integer userId);
	
	/**
	 * ユーザーIdからパスワードを取得する
	 * @param userId
	 * @return password
	 */
	public String findPassByUserId(@Param("userId") Integer userId);

	/**
	 * ユーザー情報を登録する
	 * @param users
	 */
	public void signUpUser(Users users);

	/**
	 * ユーザー情報を更新する
	 * @param users
	 */
	public void upDateUser(Users users);
	
	/**
	 * パスワードを更新する
	 * @param userId
	 * @param password
	 */
	public void upDatePassword(@Param("userId") Integer userId,
			@Param("password") String password);
	/**
	 * ユーザーを削除する
	 * @param userId
	 */
	public void deleteUser(@Param("userId") Integer userId);
}
