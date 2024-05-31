package d_tanabe.sched_mgmt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //オブジェクトが Bean 定義のソースであることを示すクラスレベルのアノテーション、Bean を宣言することができる
@EnableWebSecurity //Spring Securityを有効にする
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	SuccessHandler successHandler;

	/**
	 * セキュリティ情報のフィルタ処理
	 * セキュリティ関係の設定をする
	 * @param http
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//ログイン処理
		http.formLogin(login -> login
				.loginPage("/login").permitAll()//ログインページの指定
				.successHandler(successHandler)//ログイン成功時の画面遷移
				.usernameParameter("account_name")//ログインページのユーザーID
				.passwordParameter("password"));//ログインページのパスワード

		//ログアウト処理
		http.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout").permitAll());

		//認可の設定
		http.authorizeHttpRequests(authz -> authz
				.requestMatchers("/user/management", "/user/signup", "/user/edit")
				.hasAuthority("1")//ADMINのみが入れるページ
				.requestMatchers("/user/schedule")
				.hasAnyAuthority("2", "1")//USERも入れるページ
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()//一般的な静的ファイルパス（"/css/**"など）に対し、非ログインでのアクセスを許可
				.requestMatchers("/", "/login", "/error", "/login-error")
				.permitAll() // 全ユーザーアクセス許可
				//指定していないパスについては、ログイン済みならアクセスを許可
				.anyRequest().authenticated());

		return http.build();
	}

	/**
	 * パスワードをハッシュ化
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
