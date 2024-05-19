package d_tanabe.sched_mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログインコントローラー
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面へ遷移する
	 */
	@GetMapping("/login")
	public String getLogin() {
		return "login/login";
	}
}
