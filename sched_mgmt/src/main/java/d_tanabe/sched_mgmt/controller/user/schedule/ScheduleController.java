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
import d_tanabe.sched_mgmt.security.XSSFilter;
import d_tanabe.sched_mgmt.service.ScheduleService;
import d_tanabe.sched_mgmt.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * スケジュールコントローラー
 */
@Controller
public class ScheduleController {

	@Autowired
	private HttpSession session;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	UsersService usersService;
	@Autowired
	private XSSFilter xssFilter;

	/**
	 * スケジュール画面へ遷移する
	 * 
	 * @param user (ログイン情報を保持しているクラス)
	 * @param form (スケジュール画面からの入力をバインド)
	 * @param model
	 * @param request
	 * @return schedule/user/schedule
	 */
	@GetMapping("/user/schedule")
	public String getSignup(
		@AuthenticationPrincipal UsersDetails user,
		@ModelAttribute ScheduleForm form,
		Model model,
		HttpServletRequest request) {

		session = request.getSession();
		Object loginUser = session.getAttribute("loginUser");
		
		// nullまたは異なるユーザーの場合はセッションを設定する
		if (loginUser == null || !loginUser.equals(user.getUserName())) {
			session.setAttribute("loginUser", xssFilter.escapeStr(user.getUserName()));
			model.addAttribute("loginUser",xssFilter.escapeStr(user.getUserName()));
		}else{
			model.addAttribute("loginUser", xssFilter.escapeStr(loginUser.toString()));
		}

		Schedule schedule = new Schedule();

		// 各種(ユーザーID、アカウント名、ユーザー名)設定をする
		if (form.getUserId() != null) {

			// ユーザーIDからユーザー情報を検索して設定
			Users users = usersService.selectByUserId(form.getUserId());

			model.addAttribute("user_id", users.getId());
			model.addAttribute("account_name", xssFilter.escapeStr(users.getAccountName()));
			model.addAttribute("user_name", xssFilter.escapeStr(users.getUserName()));
			schedule.setUserId(users.getId());

		//nullの場合はログインしているユーザーの情報を設定
		} else {
			model.addAttribute("user_id", user.getUserId());
			model.addAttribute("account_name", xssFilter.escapeStr(user.getUsername()));
			model.addAttribute("user_name", xssFilter.escapeStr(user.getUserName()));
			schedule.setUserId(user.getUserId());
		}

		// スケジュールをリストとして設定
		model.addAttribute("scheduleMap", scheduleService.selectSchedule(schedule, "2023", "1"));

		return "user/schedule";
	}
}
