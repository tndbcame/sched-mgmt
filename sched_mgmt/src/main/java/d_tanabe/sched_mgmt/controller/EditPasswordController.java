package d_tanabe.sched_mgmt.controller;

import java.util.Locale;

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

import d_tanabe.sched_mgmt.form.EditPasswordForm;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.validation.CommonValidation;
import d_tanabe.sched_mgmt.validation.PasswordValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * パスワード変更画面コントローラー
 */
@Controller
public class EditPasswordController {

	//セッション
	@Autowired
	private HttpSession session;

	@Autowired
	UsersService usersService;

	//メッセージ
	@Autowired
	MessageSource messagesource;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	//パスワード画面のバリデーション
	@Autowired
	private PasswordValidation passwordValidation;

	/**
	 * パスワード変更画面へ遷移する
	 * @param form (パスワード変更画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return user/password
	 */
	@GetMapping("/editPassword")
	public String getEditPassword(@ModelAttribute EditPasswordForm form,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();
		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));

		//ユーザーIdをセット
		model.addAttribute("userId", form.getUserId());

		return "user/password";

	}

	/**
	 * パスワードを変更する
	 * @param form (パスワード変更画面からの入力をバインド)
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return redirect:/complete
	 * または getPassword(パスワード変更画面のgetメソッド)
	 */
	@PostMapping("/updatePassword")
	public String postEditUpdatePassword(@Validated @ModelAttribute EditPasswordForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return getEditPassword(form, model, request);
		}

		String errorMsg = "";

		//ユーザーの存在チェック
		//存在しない場合はエラー
		if (usersService.existsUser(form.getUserId(), form.getCurrentPassword())) {

			//新しいパスワードとその確認用のパスワードが同一ものかチェックしてメッセージを取得
			errorMsg = passwordValidation.getPasswordErrorMsg(form.getNewPassword(), form.getNewPassword2());
			if ("".equals(errorMsg)) {

				//パスワード更新
				usersService.upDatePassword(form.getUserId(), form.getNewPassword());

				//メッセージをセット
				redirectAttributes.addFlashAttribute("message",
						usersService.getCompleteMessage("4"));

				//完了画面へ遷移
				return "redirect:/complete";

			} else {

				//エラーメッセージがある場合はgetへ
				model.addAttribute("failMsg2", errorMsg);
				return getEditPassword(form, model, request);
			}

		} else {

			//エラーメッセージを格納してgetへ
			errorMsg = messagesource.getMessage(
					"002.validation.notUser", null, Locale.JAPAN);
			model.addAttribute("failMsg", errorMsg);
			return getEditPassword(form, model, request);
		}
	}
}
