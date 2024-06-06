package d_tanabe.sched_mgmt.form.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * ユーザー登録画面からの入力をバインド
 */
@Data
public class SignupForm {
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+$") //半角英数字かチェック
	@Length(min = 0, max = 10) 
	private String accountName;
	@NotBlank
	private String userName;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Length(min = 8, max = 16)
	private String password;
	private boolean admin;
}