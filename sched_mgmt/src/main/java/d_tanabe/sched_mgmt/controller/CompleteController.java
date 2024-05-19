package d_tanabe.sched_mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import d_tanabe.sched_mgmt.validation.CommonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 完了画面コントローラー
 */
@Controller
public class CompleteController {

	//セッション
	@Autowired
	private HttpSession session;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	/**
	 * 完了画面へ遷移する
	 * @param model
	 * @param request
	 * @return user/complete
	 */
	@GetMapping("/complete")
	public String getSignup(Model model,
			HttpServletRequest request) {

		// Flash Scopeから値の取り出してmodelにセット
		String message = (String) model.getAttribute("message");

		//messageがない場合はリダイレクト
		if (message == null) {
			return "redirect:/management";
		}

		//セッションを取得する
		session = request.getSession();

		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));

		return "user/complete";

	}

}
