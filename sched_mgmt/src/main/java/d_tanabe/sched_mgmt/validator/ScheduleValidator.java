package d_tanabe.sched_mgmt.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import d_tanabe.sched_mgmt.form.user.ScheduleForm;
import d_tanabe.sched_mgmt.service.ScheduleService;
import d_tanabe.sched_mgmt.util.message.MessageManager;

@Component
public class ScheduleValidator implements Validator {

    @Autowired
    MessageSource messagesource;
    @Autowired
    ScheduleService scheduleService;

    @SuppressWarnings("null")
    @Override
    public boolean supports(Class<?> clazz) {
        return ScheduleForm.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(Object target, Errors errors) {
        ScheduleForm form = (ScheduleForm) target;

        try {
            int day_ = Integer.parseInt(form.getDay());
            Integer lastDay = scheduleService.getLastDayOfTheMonth(form.getYear(), form.getMonth());

            //月の最終日より選択日付が大きい場合
            if (lastDay < day_) {
                errors.rejectValue("day", "ScheduleForm.day",
                        MessageManager.MAX_RANGE_OF_DAYS.getMessage(messagesource, lastDay));
            }
        } catch (NumberFormatException e) {
            
            //数値に変換できない場合は例外
            errors.rejectValue("day", "ScheduleForm.day",
                    MessageManager.INVALID_STR.getMessage(messagesource));
        }
    }
}
