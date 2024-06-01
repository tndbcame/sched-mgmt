package d_tanabe.sched_mgmt.controller.user.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import d_tanabe.sched_mgmt.form.user.edit.PasswordForm;
import d_tanabe.sched_mgmt.security.XSSFilter;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.util.message.MessageManager;
import d_tanabe.sched_mgmt.validator.PasswordValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordController {

    // セッション
    @Autowired
    private HttpSession session;

    // サービスクラス
    @Autowired
    private UsersService usersService;

    // 共通バリデーション
    @Autowired
    private XSSFilter xssFilter;

    // メッセージ
    @Autowired
    MessageSource messagesource;

    // パスワード画面のバリデーション
    @Autowired
    private PasswordValidator passwordValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(passwordValidator);
    }

    /**
     * パスワード変更画面へ遷移する
     * 
     * @param form (パスワード変更画面からの入力をバインド)
     * @param model
     * @param request
     * @return user/edit/password
     */
    @GetMapping("/user/edit/{userId}/password")
    public String getEditPassword(@ModelAttribute PasswordForm form, Model model,
            HttpServletRequest request) {

        // セッションを取得する
        session = request.getSession();
        model.addAttribute("loginUser",
                xssFilter.escapeStr(session.getAttribute("loginUser").toString()));

        // ユーザーIdをセット
        model.addAttribute("userId", form.getUserId());

        return "user/edit/password";
    }

    /**
     * パスワードを変更する
     * 
     * @param form (パスワード変更画面からの入力をバインド)
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @param request
     * @return redirect:/user/edit/complete または getPassword(パスワード変更画面のgetメソッド)
     */
    @PostMapping("/user/edit/password/update")
    public String postEditUpdatePassword(@Validated @ModelAttribute PasswordForm form,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // バリデーションチェック
        if (bindingResult.hasErrors()) {
            return getEditPassword(form, model, request);
        }

        // パスワード更新
        usersService.upDatePassword(form.getUserId(), form.getNewPassword());

        // メッセージをセット
        redirectAttributes.addFlashAttribute("message",
                MessageManager.PASSWORD_UPDATE_COMPLETE.getMessage(messagesource));

        // 完了画面へ遷移
        return "redirect:/user/edit/complete";
    }
}
