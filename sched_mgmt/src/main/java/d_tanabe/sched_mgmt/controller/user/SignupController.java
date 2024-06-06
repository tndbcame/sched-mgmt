package d_tanabe.sched_mgmt.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import d_tanabe.sched_mgmt.security.XSSFilter;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.util.message.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー登録コントローラー
 */
@Controller
public class SignupController {

	@Autowired
	private HttpSession session;
	@Autowired
	UsersService usersService;
	@Autowired
	private XSSFilter xssFilter;
	@Autowired
	MessageSource messagesource;

	/**
	 * ユーザー登録画面へ遷移する
	 * 
	 * @param form (ユーザー登録画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return user/signup またはredirect:/login
	 */
	@GetMapping("/user/signup")
	public String getSignup(
		@ModelAttribute SignupForm form,
		Model model,
		HttpServletRequest request) {
		session = request.getSession();

		// セッションがない場合はログアウト
		if (session.getAttribute("loginUser") == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginUser",
				xssFilter.escapeStr(session.getAttribute("loginUser").toString()));
		return "user/signup";
	}

	/**
	 * 新たにユーザーを登録する
	 * 
	 * @param form (ユーザー登録画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return redirect:/user/edit/complete またはgetSignup(ユーザー登録画面のgetメソッド)
	 */
	@PostMapping("/user/signup")
	public String postSignup(
		@Validated @ModelAttribute SignupForm form,
		BindingResult bindingResult,
		Model model, 
		RedirectAttributes redirectAttributes,
		HttpServletRequest request) {

		// バリデーション
		if (bindingResult.hasErrors()) {
			return getSignup(form, model, request);
		}

		redirectAttributes.addFlashAttribute("message",
				MessageManager.UPDATE_COMPLETE.getMessage(messagesource));

		// 入力値を設定して登録する
		Users users = new Users();
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());
		users.setPassword(form.getPassword());
		usersService.signupUser(users, form.isAdmin());

		return "redirect:/user/edit/complete";
	}
}
