package d_tanabe.sched_mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import d_tanabe.sched_mgmt.config.UsersDetails;
import d_tanabe.sched_mgmt.form.UserDetailForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.validation.CommonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー詳細画面コントローラー
 */
@Controller
public class UsersDetailController {

	//セッション
	@Autowired
	private HttpSession session;

	//サービスクラス
	@Autowired
	private UsersService usersService;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	/**
	 * ユーザー詳細画面へ遷移する
	 * @param form (詳細画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return user/detail
	 */
	@GetMapping("/userDetail")
	public String getUsersDetail(@ModelAttribute UserDetailForm form,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();

		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));

		//ユーザーIDからユーザー情報を検索する
		Users users = usersService.findByUserId(form.getUserId());

		model.addAttribute("userId", users.getId());
		model.addAttribute("accountName",
				commonValidation.escapeStr(users.getAccountName()));
		model.addAttribute("userName",
				commonValidation.escapeStr(users.getUserName()));
		if ("ADMIN".equals(users.getRole())) {
			model.addAttribute("admin", true);
		}
		if ("INVALID".equals(users.getStatus())) {
			model.addAttribute("status", true);
		}

		return "user/detail";
	}

	/**
	 * ユーザー情報を更新する
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (詳細画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return getUsersDetail(詳細画面のゲットメソッド)
	 * またはredirect:/logout
	 * またはredirect:/complete
	 */
	@PostMapping("/updateUser")
	public String postUpdateUser(@AuthenticationPrincipal UsersDetails user,
			@Validated @ModelAttribute UserDetailForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		//バリデーション
		if (bindingResult.hasErrors()) {
			return getUsersDetail(form, model, request);
		}

		Users users = new Users();
		//入力値を格納
		users.setId(form.getUserId());
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());

		//ユーザー情報を更新する
		usersService.upDateUser(users, form.isAdmin(), form.isStatus());

		//メッセージをセットする
		redirectAttributes.addFlashAttribute("message",
				usersService.getCompleteMessage("2"));

		//更新者が自分の情報を更新したときはログアウト
		if (form.getUserId() == user.getUserId()) {
			if (!form.isAdmin() || form.isStatus()) {
				return "redirect:/logout";
			}
		}
		//完了画面へ遷移
		return "redirect:/complete";
	}

	/**
	 * ユーザーを削除する
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (詳細画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return getUsersDetail(詳細画面のゲットメソッド)
	 * またはredirect:/logout
	 * またはredirect:/complete
	 */
	@PostMapping("/deleteUser")
	public String postDeleteUser(@AuthenticationPrincipal UsersDetails user,
			@Validated @ModelAttribute UserDetailForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		//バリデーション
		if (bindingResult.hasErrors()) {
			return getUsersDetail(form, model, request);
		}

		//ユーザーを削除
		usersService.deleteUser(form.getUserId());

		//メッセージをセットする
		redirectAttributes.addFlashAttribute("message",
				usersService.getCompleteMessage("3"));

		//更新社が自分の情報を削除したときはログアウト
		if (form.getUserId() == user.getUserId()) {
			if (!form.isAdmin() || form.isStatus()) {
				return "redirect:/logout";
			}
		}
		//完了画面へ遷移
		return "redirect:/complete";
	}
}
