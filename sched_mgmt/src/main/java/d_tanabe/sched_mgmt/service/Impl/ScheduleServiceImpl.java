package d_tanabe.sched_mgmt.service.Impl;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import d_tanabe.sched_mgmt.model.Schedule;
import d_tanabe.sched_mgmt.repository.ScheduleMapper;
import d_tanabe.sched_mgmt.service.ScheduleService;

/**
 * スケジュールサービスの実装クラス
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	ScheduleMapper scheduleMapper;

	/**
	 * スケジュールを登録する
	 */
	@Override
	public void insertSchedule(Schedule schedule, String year, String month, String day) {

		//日付型に変換して日付を設定
		schedule.setDate(convertToDate(year, month, day));

		//スケジュールを登録
		scheduleMapper.insertSchedule(schedule);
	}

	/**
	 * スケジュールを取得する
	 */
	@Override
	public HashMap<Integer, String> selectSchedule(Schedule schedule, String year, String month) {

		//日付の最小値を設定
		Date minimumDate = convertToDate(year, month, "1");

		//日付の最大値を設定
		Date maximumDate = convertToDate(year, month,
				getLastDayOfTheMonth(year, month).toString());

		//スケジュールリストから日にちとスケジュールのマップを作成
		HashMap<Integer, String> scheduleMap = createScheduleMap(
				scheduleMapper.selectSchedule(
						schedule.getUserId(), minimumDate, maximumDate));
		return scheduleMap;
	}

	/**
	 * その月の末日を取得
	 * バリデーションでも使用するためpublicで定義
	 */
	public Integer getLastDayOfTheMonth(String year, String month) {
		int _year = Integer.parseInt(year);
		int _month = Integer.parseInt(month);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, _year);
		cal.set(Calendar.MONTH, _month - 1);
		return cal.getActualMaximum(Calendar.DATE);

	}

	/**
	 * 受け取ったString型の年月日からDate型へ変換する
	 * @param year
	 * @param month
	 * @param day
	 * @return Date 日付(yyyy-MM-dd)
	 */
	private Date convertToDate(String year, String month, String day) {
		String inpDateStr = year + "-" + month + "-" + day;
		return Date.valueOf(inpDateStr);
	}

	/**
	 * (key:日 value:スケジュール)のマップを作成
	 * @param scheduleList
	 * @return map(key:日 value:スケジュール)
	 */
	private HashMap<Integer, String> createScheduleMap(List<Schedule> scheduleList) {

		HashMap<Integer, String> scheduleMap = new HashMap<Integer, String>();
		//マップに日付とスケジュールをセットする
		for (Schedule schedule : scheduleList) {

			//日付を抽出
			Integer day = extractDayFromDate(schedule.getDate());

			//その日付の予定がすでに存在している場合はカンマを付与
			if (scheduleMap.containsKey(day)) {
				String _schedule = scheduleMap.get(day) + ',';
				scheduleMap.put(day, _schedule + schedule.getSchedule());

				//それ以外は単に予定を追加する
			} else {
				scheduleMap.put(day, schedule.getSchedule());
			}
		}
		return scheduleMap;
	}

	/**
	 * 日付から日にちを抽出する
	 * @param date
	 * @return Integer day
	 */
	private Integer extractDayFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

}