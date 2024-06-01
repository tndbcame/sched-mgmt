package d_tanabe.sched_mgmt.util.message;

import java.util.Locale;
import org.springframework.context.MessageSource;

public enum MessageManager {

    MAX_RANGE_OF_DAYS("MaxRangeOfDays"),
    IS_NOT_USER("IsNotUser"),
    IS_NOT_NEW_PASSWORD("IsNotNewPassword"),
    DELETE_FAIRED("DeleteFailed"),
    INVALID_STR("InvalidStr"),
    REGISTRARION_COMPLETE("complete", "登録"),
    UPDATE_COMPLETE("complete", "更新"),
    DELETE_COMPLETE("complete", "削除"),
    PASSWORD_UPDATE_COMPLETE("complete", "パスワードの更新");

    private String key;

    private Object[] params;

    private MessageManager(String key) {
        this.key = key;
    }

    private MessageManager(String key, Object... params) {
        this.key = key;
        this.params = params;
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(key, params, Locale.JAPAN);
    }

    public String getMessage(MessageSource messageSource, Object... params) {
        return messageSource.getMessage(key, params, Locale.JAPAN);
    }
}
