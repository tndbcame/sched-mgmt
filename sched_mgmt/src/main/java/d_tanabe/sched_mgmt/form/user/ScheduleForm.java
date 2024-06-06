package d_tanabe.sched_mgmt.form.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * スケジュール画面からの入力をバインド
 */
@Data
public class ScheduleForm {
	private Integer userId;
	private String accountName;
	@Min(2023)
	@Max(2030)
	private String year;
	@Min(1)
	@Max(12)
	private String month;
	@Min(1)
	private String day;
	@NotBlank
	private String schedule;
	/**
	 * 	エラー時のメッセージを格納
	 */
	private String errorMsg;
}
