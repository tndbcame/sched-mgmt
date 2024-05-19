package d_tanabe.sched_mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

/**
 * ログアウトコントローラー
 */
@Controller
public class LogoutController {

	// セッション
	@Autowired
	private HttpSession session;

	/**
	 * ログアウトしてログイン画面へ遷移する
	 */
	@PostMapping("/logout")
	public String postLogout() {

		//セッション削除
		session.removeAttribute("loginUser");
		return "login/login";
	}

}
