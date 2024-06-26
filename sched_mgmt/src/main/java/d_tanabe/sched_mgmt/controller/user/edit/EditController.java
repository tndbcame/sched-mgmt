package d_tanabe.sched_mgmt.controller.user.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import d_tanabe.sched_mgmt.form.user.edit.EditForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.security.XSSFilter;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.util.message.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー編集コントローラー
 */
@Controller
public class EditController {
	
	@Autowired
	private HttpSession session;
	@Autowired
	private UsersService usersService;
	@Autowired
	private XSSFilter xssFilter;
	@Autowired
	MessageSource messagesource;

	/**
	 * ユーザー編集画面へ遷移する
	 * 
	 * @param form (編集画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return /user/edit/edit
	 */
	@GetMapping("/user/edit/{userId}")
	public String getEdit(
		@ModelAttribute EditForm form,
		Model model,
		HttpServletRequest request) {

		// セッションを取得する
		session = request.getSession();
		model.addAttribute("loginUser",
			xssFilter.escapeStr(session.getAttribute("loginUser").toString()));

		// ユーザーIDからユーザー情報を検索する
		Users users = usersService.selectByUserId(form.getUserId());

		model.addAttribute("userId", users.getId());
		model.addAttribute("accountName", xssFilter.escapeStr(users.getAccountName()));
		model.addAttribute("userName", xssFilter.escapeStr(users.getUserName()));

		//権限が1の時trueする
		if ("1".equals(users.getRole())){
			model.addAttribute("admin", true);
		}
		//ステータスが2の時をtrueする
		if ("2".equals(users.getStatus())){
			model.addAttribute("status", true);
		}
		return "/user/edit/edit";
	}

	/**
	 * ユーザー情報を更新する
	 * 
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (編集画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return getEdit(),/logout,/user/edit/completeの内どれか
	 */
	@PostMapping("/user/edit/update")
	public String postupdateUser(
		@AuthenticationPrincipal UsersDetails user,
		@Validated @ModelAttribute EditForm form,
		BindingResult bindingResult,
		Model model,
		RedirectAttributes redirectAttributes,
		HttpServletRequest request) {

		// バリデーション
		if (bindingResult.hasErrors()){
			return getEdit(form, model, request);
		}
		Users users = new Users();
		users.setId(form.getUserId());
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());

		// ユーザー情報を更新する
		usersService.updateUser(users, form.isAdmin(), form.isStatus());

		redirectAttributes.addFlashAttribute("message",
				MessageManager.UPDATE_COMPLETE.getMessage(messagesource));

		// 更新者が自分の情報を更新したときはログアウト
		if (form.getUserId() == user.getUserId()) {
			if (!form.isAdmin() || form.isStatus()) {
				return "redirect:/logout";
			}
		}
		// 完了画面へ遷移
		return "redirect:/user/edit/complete";
	}

	/**
	 * ユーザーを削除する
	 * 
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (編集画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return getEdit(編集画面のゲットメソッド) またはredirect:/logout またはredirect:/user/edit/complete
	 */
	@PostMapping("/user/edit/delete")
	public String postDeleteUser(
		@AuthenticationPrincipal UsersDetails user,
		@Validated @ModelAttribute EditForm form,
		BindingResult bindingResult,
		Model model,
		RedirectAttributes redirectAttributes,
		HttpServletRequest request) {

		// バリデーション
		if (bindingResult.hasErrors()){
			return getEdit(form, model, request);
		}
		// ユーザーを削除
		usersService.deleteUser(form.getUserId());

		redirectAttributes.addFlashAttribute("message",
				MessageManager.DELETE_COMPLETE.getMessage(messagesource));

		// 更新社が自分の情報を削除したときはログアウト
		if (form.getUserId() == user.getUserId()) {
			if (!form.isAdmin() || form.isStatus()) {
				return "redirect:/logout";
			}
		}
		// 完了画面へ遷移
		return "redirect:/user/edit/complete";
	}

	/**
	 * 完了画面へ遷移する
	 * 
	 * @param model
	 * @param request
	 * @return user/user/edit/complete
	 */
	@GetMapping("/user/edit/complete")
	public String getSignup(Model model, HttpServletRequest request) {

		// Flash Scopeから値の取り出してmodelにセット
		String message = (String) model.getAttribute("message");

		// messageがない場合はリダイレクト
		if (message == null) {
			return "redirect:/user/management";
		}
		session = request.getSession();
		model.addAttribute("loginUser",
				xssFilter.escapeStr(session.getAttribute("loginUser").toString()));

		return "user/edit/complete";

	}
}
