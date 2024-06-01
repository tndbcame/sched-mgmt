package d_tanabe.sched_mgmt.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 認証成功時のハンドラ
 */
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * ログイン成功後の画面遷移
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		//コネクション型からリストへ変換
		List<GrantedAuthority> authenticationList = new ArrayList<>(authentication.getAuthorities());

		//権限がADMINかチェック
		if ("1".equals(authenticationList.get(0).getAuthority())) {
			response.sendRedirect(request.getContextPath() + "/user/management");

			//権限が無効の場合
		} else if ("2".equals(authenticationList.get(0).getAuthority())) {
			response.sendRedirect(request.getContextPath() + "/user/schedule");

		} else {
			response.sendRedirect(request.getContextPath() + "/login");
		}
	}

}
