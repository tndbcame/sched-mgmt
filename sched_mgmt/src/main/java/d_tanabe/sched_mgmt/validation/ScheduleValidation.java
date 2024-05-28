package d_tanabe.sched_mgmt.validation;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import d_tanabe.sched_mgmt.service.ScheduleService;



/**
 * スケジュールのバリデーションクラス
 */
@Component
public class ScheduleValidation {

	@Autowired
	ScheduleService scheduleService;

	// メッセージ
	@Autowired
	MessageSource messagesource;

	/**
	 * 日付入力チェックしてエラーメッセージを返却
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return msg(エラーメッセージ)
	 */
	public String getDayOfTheLastMonthErrorMsg(String year, String month, String day) {
		String msg = "";

		try {
			// 日付を数値型へ変換
			int day_ = Integer.parseInt(day);
			Integer lastDay = scheduleService.getLastDayOfTheMonth(year, month);
			if (lastDay < day_) {
				// 日付のメッセージを取得する
				msg = messagesource.getMessage("001.validation.maxRangeOfDays",
						new String[] {lastDay.toString()}, Locale.JAPAN);
			}
		} catch (Exception e) {
			msg = messagesource.getMessage("005.validation.invalidStr", null, Locale.JAPAN);
		}

		return msg;

	}
}
