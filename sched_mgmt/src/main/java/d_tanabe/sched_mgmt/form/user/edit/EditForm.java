package d_tanabe.sched_mgmt.form.user.edit;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * ユーザー編集画面からの入力をバインド
 */
@Data
public class EditForm {
	@NotNull
	private Integer userId;
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Length(min = 0, max = 10)
	private String accountName;
	@NotBlank
	private String userName;
	private boolean admin;
	private boolean status;
	
	//非同期通信用のString型ID
	private String userIdStr;
	
	//エラー時のメッセージを格納
	private String errorMsg;
}
