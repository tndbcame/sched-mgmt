package d_tanabe.sched_mgmt.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import d_tanabe.sched_mgmt.form.user.edit.PasswordForm;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.util.message.MessageManager;
@Component
public class PasswordValidator implements Validator  {

	@Autowired
	MessageSource messagesource;

    @Autowired
	private UsersService usersService;
	
    @SuppressWarnings("null")
    @Override
	public boolean supports(Class<?> clazz) {
		return PasswordForm.class.isAssignableFrom(clazz);
	}

    @SuppressWarnings("null")
    @Override
	public void validate(Object target, Errors errors) {
		PasswordForm form = (PasswordForm)target;

        if(!usersService.existsUser(form.getUserId(), form.getCurrentPassword())){
            errors.rejectValue("currentPassword",
            "PasswordForm.currentPassword",
            MessageManager.IS_NOT_USER.getMessage(messagesource));
        }
		if(!form.getNewPassword().equals(form.getNewPassword2())) {
            errors.rejectValue("newPassword",
            "PasswordForm.newPassword",
            MessageManager.IS_NOT_NEW_PASSWORD.getMessage(messagesource));
		}
	}
}
