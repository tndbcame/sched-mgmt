package d_tanabe.sched_mgmt.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import d_tanabe.sched_mgmt.model.Users;


/**
 * ログインした時のユーザー情報がこのクラスで保持される
 */
public class UsersDetails implements UserDetails {

	private final Users user;

	/**
	 * ユーザーの権限を扱うためのクラス
	 */
	private Collection<GrantedAuthority> authorities;

	public UsersDetails(Users user) {
		this.user = user;
	}

	/**
	 * ユーザーの権限を取得
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if (authorities != null) {
			return authorities;
		}
		String role = this.user.getRole();

		// 無効ユーザーの場合は権限が無効になるように設定
		if ("2".equals(this.user.getStatus())) {
			role = this.user.getStatus();
		}

		// 権限付与
		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		authorities = new ArrayList<>();
		authorities.add(authority);
		return authorities;
	}

	/**
	 * ユーザーIdを取得(独自メソッド)
	 * 
	 * @return Integer
	 */
	public Integer getUserId() {
		return user.getId();
	}

	/**
	 * UserDetailsクラスのパスワード取得
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * UserDetailsクラスのユーザー名取得ここではアカウント名
	 */
	@Override
	public String getUsername() {
		return user.getAccountName();
	}

	/**
	 * アカウント名とは別に定義されたユーザー名を取得(独自メソッド)
	 * 
	 * @return String
	 */
	public String getUserName() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
