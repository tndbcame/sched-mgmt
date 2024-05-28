package d_tanabe.sched_mgmt.form.user.edit;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * パスワード変更画面からの入力をバインド
 */
@Data
public class PasswordForm {

	private Integer userId;
	@NotBlank
	private String currentPassword;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Length(min = 8, max = 16)
	private String newPassword;
	@NotBlank
	private String newPassword2;
}
