package d_tanabe.sched_mgmt.controller.user.schedule;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import d_tanabe.sched_mgmt.form.user.ScheduleForm;
import d_tanabe.sched_mgmt.model.Schedule;
import d_tanabe.sched_mgmt.security.XSSFilter;
import d_tanabe.sched_mgmt.service.ScheduleService;
import d_tanabe.sched_mgmt.validator.ScheduleValidator;


/**
 * Restスケジュールコントローラー(非同期通信用の処理)
 */
@RestController
public class ScheduleRestController {

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	MessageSource massageSource;

	@Autowired
	ScheduleValidator scheduleValidator;

	// 共通バリデーション
	@Autowired
	private XSSFilter xssFilter;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(scheduleValidator);
	}

	/**
	 * スケジュールを登録する
	 * 
	 * @param form (スケジュール画面からの入力をバインド)
	 * @param bindingResult
	 * @return form
	 */
	@PostMapping("/user/schedule/register")
	public ScheduleForm insertSchedule(@Validated @RequestBody ScheduleForm form,
			BindingResult bindingResult) {

		Schedule schedule = new Schedule();

		// バリデーションチェック
		if (bindingResult.hasErrors()) {

			String errorMessages = "";

			// formでチェックしたエラーはここで取得
			if (bindingResult.hasErrors()) {
				for (ObjectError error : bindingResult.getAllErrors()) {
					// エラーメッセージ取得
					errorMessages += "\n" + error.getDefaultMessage();
				}
			}

			// エラーメッセージをセットして返却
			form.setErrorMsg(errorMessages);
			return form;
		}

		// スケージュールをセット
		schedule.setId(form.getUserId());
		schedule.setUserId(form.getUserId());
		schedule.setSchedule(form.getSchedule());

		// スケージュールを登録
		scheduleService.insertSchedule(schedule, form.getYear(), form.getMonth(), form.getDay());

		// スケジュール内容は不正文字列がないかエスケープする
		form.setSchedule(xssFilter.escapeStr(form.getSchedule()));

		return form;
	}

	/**
	 * スケジュールを表示する
	 * 
	 * @param form (スケジュール画面からの入力をバインド)
	 * @return scheduleMap(key:Days value:schedule)
	 */
	@PostMapping("/user/schedule/disp")
	public HashMap<Integer, String> diplaySchedule(@RequestBody ScheduleForm form) {

		Schedule schedule = new Schedule();

		// ユーザーIDを設定をする
		schedule.setUserId(form.getUserId());

		// スケージュールマップを取得
		HashMap<Integer, String> scheduleMap =
				scheduleService.selectSchedule(schedule, form.getYear(), form.getMonth());

		// スケジュールを取得時にエスケープ処理をしてから返却
		for (Entry<Integer, String> entry : scheduleMap.entrySet()) {
			scheduleMap.put(entry.getKey(), xssFilter.escapeStr(entry.getValue()));
		}

		return scheduleMap;
	}

}
