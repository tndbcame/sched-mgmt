package d_tanabe.sched_mgmt.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * ユーザー登録画面からの入力をバインド
 */
@Data
public class SignUpForm {
	@NotBlank //文字列がnull,空文字,空白スペースでないかチェック
	@Pattern(regexp = "^[a-zA-Z0-9]+$") //半角英数字かチェック
	@Length(min = 0, max = 10) //規定の文字数かチェック
	private String accountName;
	@NotBlank
	private String userName;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Length(min = 8, max = 16)
	private String password;
	private boolean admin;
}