package d_tanabe.sched_mgmt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
