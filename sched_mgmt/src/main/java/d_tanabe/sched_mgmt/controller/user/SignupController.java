package d_tanabe.sched_mgmt.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import d_tanabe.sched_mgmt.form.user.SignupForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.validation.CommonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー登録コントローラー
 */
@Controller
public class SignupController {

	//セッション
	@Autowired
	private HttpSession session;

	@Autowired
	UsersService usersService;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	/**
	 * ユーザー登録画面へ遷移する
	 * @param form (ユーザー登録画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return user/signup
	 * またはredirect:/login
	 */
	@GetMapping("/user/signup")
	public String getSignup(@ModelAttribute SignupForm form,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();

		//セッションがない場合はログアウト
		if (session.getAttribute("loginUser") == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));
		return "user/signup";
	}

	/**
	 * 新たにユーザーを登録する
	 * @param form (ユーザー登録画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return redirect:/user/edit/complete
	 * またはgetSignup(ユーザー登録画面のgetメソッド)
	 */
	@PostMapping("/user/signup")
	public String postSignup(@Validated @ModelAttribute SignupForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return getSignup(form, model, request);
		}

		//メッセージをセット
		redirectAttributes.addFlashAttribute("message",
				usersService.getCompleteMessage("1"));

		//入力値を格納して登録する
		Users users = new Users();
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());
		users.setPassword(form.getPassword());

		//ユーザーを登録する
		usersService.signUpUser(users, form.isAdmin());

		//完了画面へ遷移
		return "redirect:/user/edit/complete";

	}
}
