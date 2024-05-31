package d_tanabe.sched_mgmt.controller.user.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import d_tanabe.sched_mgmt.config.UsersDetails;
import d_tanabe.sched_mgmt.form.user.ScheduleForm;
import d_tanabe.sched_mgmt.model.Schedule;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.ScheduleService;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.validation.CommonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * スケジュールコントローラー
 */
@Controller
public class ScheduleController {

	//セッション
	@Autowired
	private HttpSession session;

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	UsersService usersService;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	/**
	 * スケジュール画面へ遷移する
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (スケジュール画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return schedule/user/schedule
	 */
	@GetMapping("/user/schedule")
	public String getSignup(@AuthenticationPrincipal UsersDetails user,
			@ModelAttribute ScheduleForm form,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();
		model.addAttribute("loginUser", session.getAttribute("loginUser"));

		//nullの場合
		if (session.getAttribute("loginUser") == null) {
			System.out.println("新しくセッションを設定");
			//セッションにユーザー名を設定する
			session.setAttribute("loginUser", user.getUserName());

			//ログインユーザーが異なる場合
		} else if (!session.getAttribute("loginUser").equals(user.getUserName())) {
			session.setAttribute("loginUser",
					commonValidation.escapeStr(user.getUserName().toString()));
		}
		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));

		Schedule schedule = new Schedule();

		//各種(ユーザーID、アカウント名、ユーザー名)設定をする
		//フォームのユーザーIDがnullの場合はログインしているユーザーの情報を設定
		if (form.getUserId() != null) {

			//ユーザーIDからユーザー情報を検索する
			Users users = usersService.findByUserId(form.getUserId());

			model.addAttribute("user_id", users.getId());
			model.addAttribute("account_name",
					commonValidation.escapeStr(users.getAccountName()));
			model.addAttribute("user_name",
					commonValidation.escapeStr(users.getUserName()));
			schedule.setUserId(users.getId());
		} else {
			model.addAttribute("user_id", user.getUserId());
			model.addAttribute("account_name",
					commonValidation.escapeStr(user.getUsername()));
			model.addAttribute("user_name",
					commonValidation.escapeStr(user.getUserName()));
			schedule.setUserId(user.getUserId());
		}

		//スケジュールをリストとして設定
		model.addAttribute("scheduleMap", scheduleService.selectSchedule(schedule, "2023", "1"));

		return "user/schedule";
	}
}
