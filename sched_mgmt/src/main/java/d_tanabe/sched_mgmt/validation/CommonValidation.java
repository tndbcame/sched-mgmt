package d_tanabe.sched_mgmt.validation;

import org.springframework.stereotype.Component;

/**
 * 共通バリデーションクラス
 */
@Component
public class CommonValidation {

	/**
	 * クロスサイトスクライピング対策
	 * HTMLのタグに関係ある文字列をエスケープする
	 * @param targetValue
	 * @return String エスケープした文字列
	 */
	public String escapeStr(String targetValue) {
		if (targetValue == null)
			return "";
		targetValue = targetValue.replaceAll("&", "&amp;");
		targetValue = targetValue.replaceAll("<", "&lt;");
		targetValue = targetValue.replaceAll(">", "&gt;");
		targetValue = targetValue.replaceAll("\"", "&quot;");
		targetValue = targetValue.replaceAll("'", "&apos;");
		return targetValue;
	}

}
