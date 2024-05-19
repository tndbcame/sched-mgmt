package d_tanabe.sched_mgmt.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import d_tanabe.sched_mgmt.model.Schedule;


/**
 * スケジュールTBLマッパー
 */
@Mapper
public interface ScheduleMapper {

	/**
	 * 予定を登録する
	 * @param schedule
	 */
	public void insertSchedule(Schedule schedule);

	/**
	 * 選択した日付の予定を表示する
	 * @param userId
	 * @param minimumDate
	 * @param maximumDate
	 * @return List(スケジュール情報)
	 */
	public List<Schedule> selectSchedule(@Param("userId") Integer userId,
			@Param("minimumDate") Date minimumDate,
			@Param("maximumDate") Date maximumDate);

}
