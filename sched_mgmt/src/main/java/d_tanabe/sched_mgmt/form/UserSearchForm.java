package d_tanabe.sched_mgmt.form;

import lombok.Data;

/**
 * ユーザー検索画面からの入力をバインド
 */
@Data
public class UserSearchForm {
	private Integer userId;
	private String accountName;
	private String userName;
	private boolean status;
	private Integer currentPage;
}
