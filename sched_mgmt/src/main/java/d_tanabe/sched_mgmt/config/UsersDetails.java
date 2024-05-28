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
		//ユーザーの権限を取得して格納する
		String role = this.user.getRole();

		//無効ユーザーの場合は権限が無効になるように設定
		if ("2".equals(this.user.getStatus())) {
			role = this.user.getStatus();
		}

		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		authorities = new ArrayList<>();
		authorities.add(authority);
		return authorities;
	}

	/**
	 * ユーザーIdを取得する
	 * (独自メソッド)
	 * @return Integer
	 */
	public Integer getUserId() {
		return user.getId();
	}

	/**
	 * UserDetailsクラスのパスワード取得メソッド
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * UserDetailsクラスのユーザーネーム取得メソッド
	 * ここではアカウント名
	 */
	@Override
	public String getUsername() {
		return user.getAccountName();
	}

	/**
	 * アカウント名とは別に定義されたユーザーネームを取得
	 * (独自メソッド)
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
