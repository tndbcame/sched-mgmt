package d_tanabe.sched_mgmt.model;

import lombok.Data;

/**
 * ユーザーTBLとマッピングする
 */
@Data
public class Users {
	private Integer id;
	private String accountName;
	private String userName;
	private String password;
	private String role;
	private String status;
	private Integer totalUsers;
}
