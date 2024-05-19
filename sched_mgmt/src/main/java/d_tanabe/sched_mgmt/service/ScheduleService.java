package d_tanabe.sched_mgmt.service;

import java.util.HashMap;

import d_tanabe.sched_mgmt.model.Schedule;


public interface ScheduleService {

	/**
	 * スケジュールを登録する
	 * @param schedule
	 * @param year
	 * @param month
	 * @param day
	 */
	public void insertSchedule(Schedule schedule, String year, String month, String day);

	/**
	 * ユーザーのスケージュールを検索して選択した月のスケジュールを取得
	 * @param schedule
	 * @param year
	 * @param month
	 * @return HashMap(Integer day, String スケジュール) 
	 */
	public HashMap<Integer, String> selectSchedule(Schedule schedule, String year, String month);

	/**
	 * 月末の日を取得する
	 * @param year
	 * @param month
	 * @return Integer(月末の日)
	 */
	public Integer getLastDayOfTheMonth(String year, String month);
}
