package d_tanabe.sched_mgmt.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import d_tanabe.sched_mgmt.config.UsersDetails;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.repository.UsersMapper;


/**
 * UserDetailsServiceの実装クラス
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	public UsersMapper usersMapper;

	/**
	 * 入力されたログイン情報がDBに存在するからチェックし
	 * ログイン情報をUserDetailsへ格納しを返却する
	 */
	@Override
	public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {

		//ログイン情報取得
		Users user = usersMapper.findByAccountName(accountName);

		//取得したデータが無い場合はエラーで落ちる
		if (user == null) {
			throw new UsernameNotFoundException("not found : " + accountName);
		}
		return new UsersDetails(user);
	}
}
