package d_tanabe.sched_mgmt.model;

import java.sql.Date;
import lombok.Data;

/**
 * スケジュールTBLとマッピングする
 */
@Data
public class Schedule {
	private Integer id;
	private Integer userId;
	private Date date;
	private String schedule;
}
