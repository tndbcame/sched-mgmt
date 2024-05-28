package d_tanabe.sched_mgmt.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import d_tanabe.sched_mgmt.config.UsersDetails;
import d_tanabe.sched_mgmt.form.user.ManagementForm;
import d_tanabe.sched_mgmt.model.Users;
import d_tanabe.sched_mgmt.service.UsersService;
import d_tanabe.sched_mgmt.validation.CommonValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー管理画面コントローラー
 */
@Controller
public class ManagementController {

	//セッション
	@Autowired
	private HttpSession session;

	//サービスクラス
	@Autowired
	private UsersService usersService;

	//共通バリデーション
	@Autowired
	private CommonValidation commonValidation;

	//一ページあたりに表示するデータ数
	private final int perPage = 10;

	//表示するユーザーリストの開始位置のインデックスを設定
	private int startIndex = 0;

	/**
	 * ユーザー管理画面へ遷移する
	 * @param form (ユーザー管理画面からの入力情報をバインド)
	 * @param user (ログイン情報を保持しているクラス)
	 * @param model
	 * @param request
	 * @return user/management
	 */
	@GetMapping("/management")
	public String getManagement(@ModelAttribute ManagementForm form,
			@AuthenticationPrincipal UsersDetails user,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();

		//nullの場合
		if (session.getAttribute("loginUser") == null) {
			System.out.println("新しくセッションを設定");
			//セッションにユーザー名を設定する
			session.setAttribute("loginUser", user.getUserName());

			//ログインユーザーが異なる場合
		} else if (!session.getAttribute("loginUser").equals(user.getUserName())) {
			session.setAttribute("loginUser", user.getUserName());
		}

		model.addAttribute("loginUser",
				commonValidation.escapeStr(session.getAttribute("loginUser").toString()));

		Users users = new Users();

		//初期表示は無効ユーザーも含んだ状態で検索
		users.setStatus("2");

		//検索条件(初期値)を設定する
		model.addAttribute("userIdSearchCondition", form.getUserId());
		model.addAttribute("userNameSearchCondition", "");
		model.addAttribute("accountNameSearchCondition", "");
		model.addAttribute("statusSearchCondition", true);
		model.addAttribute("currentPage", 1);

		//ユーザーを全件取得してセット
		List<Users> usersList = usersService.selectByUser(users, perPage, startIndex);
		model.addAttribute("usersList", usersList);

		//表示するページ数をセット
		System.out.println(perPage + usersList.get(0).getTotalUsers());
		int totalPage = (int) Math.ceil(
				usersList.get(0).getTotalUsers() / (double) perPage);
		model.addAttribute("totalPage", totalPage);

		//次へと前へを設定する
		model.addAttribute("prevPage", 1);
		if (totalPage == 1) {
			model.addAttribute("nextPage", totalPage);
		} else {
			model.addAttribute("nextPage", 2);
		}

		return "user/management";

	}

	/**
	 * ユーザーを検索する
	 * @param form (ユーザー管理画面からの入力情報をバインド)
	 * @param user (ログイン情報を保持しているクラス)
	 * @param model
	 * @param request
	 * @return user/management
	 */
	@GetMapping("/userSearch")
	public String searchUsers(@ModelAttribute ManagementForm form,
			@AuthenticationPrincipal UsersDetails user,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();
		model.addAttribute("loginUser", session.getAttribute("loginUser"));

		Users users = new Users();

		//ユーザー情報をセットする
		users.setId(form.getUserId());
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());
		if (form.isStatus()) {
			users.setStatus("2");
		}

		//検索条件は保持
		model.addAttribute("userIdSearchCondition", form.getUserId());
		model.addAttribute("userNameSearchCondition", form.getUserName());
		model.addAttribute("accountNameSearchCondition", form.getAccountName());
		model.addAttribute("statusSearchCondition", form.isStatus());
		model.addAttribute("currentPage", 1);

		//ユーザーを全件取得してセット
		List<Users> usersList = usersService.selectByUser(users, perPage, startIndex);
		model.addAttribute("usersList", usersList);

		//表示するページ数をセット
		int totalPage = 1;
		if (usersList.isEmpty()) {
			model.addAttribute("totalPage", totalPage);
		} else {
			totalPage = (int) Math.ceil(
					usersList.get(0).getTotalUsers() / (double) perPage);
			model.addAttribute("totalPage", totalPage);
		}

		//次へと前へを設定する
		model.addAttribute("prevPage", 1);
		if (totalPage == 1) {
			model.addAttribute("nextPage", totalPage);
		} else {
			model.addAttribute("nextPage", 2);
		}

		//スケージュールマップを返却
		return "user/management";
	}

	/**
	 * ページング(数字を押下した時の処理)
	 * @param form (ユーザー管理画面からの入力情報をバインド)
	 * @param user (ログイン情報を保持しているクラス)
	 * @param model
	 * @param request
	 * @return user/management
	 */
	@GetMapping("/pagingUsers")
	public String pagingUsers(@ModelAttribute ManagementForm form,
			@AuthenticationPrincipal UsersDetails user,
			Model model,
			HttpServletRequest request) {

		//セッションを取得する
		session = request.getSession();
		model.addAttribute("loginUser", session.getAttribute("loginUser"));

		Users users = new Users();

		//ユーザー情報をセットする
		users.setId(form.getUserId());
		users.setAccountName(form.getAccountName());
		users.setUserName(form.getUserName());
		if (form.isStatus()) {
			users.setStatus("2");
		}

		//検索条件は保持
		model.addAttribute("userIdSearchCondition", form.getUserId());
		model.addAttribute("userNameSearchCondition", form.getUserName());
		model.addAttribute("accountNameSearchCondition", form.getAccountName());
		model.addAttribute("statusSearchCondition", form.isStatus());

		//今いるページ
		startIndex = (form.getCurrentPage() - 1) * perPage;
		model.addAttribute("currentPage", form.getCurrentPage());

		//ユーザーを全件取得してセット
		List<Users> usersList = usersService.selectByUser(users, perPage, startIndex);
		model.addAttribute("usersList", usersList);

		//表示するページ数をセット
		int totalPage = 1;
		if (usersList.isEmpty()) {
			model.addAttribute("totalPage", totalPage);
		} else {
			totalPage = (int) Math.ceil(
					usersList.get(0).getTotalUsers() / (double) perPage);
			model.addAttribute("totalPage", totalPage);
		}

		//次へと前へを設定する
		if (form.getCurrentPage() == 1) {
			model.addAttribute("prevPage", form.getCurrentPage());
		} else {
			model.addAttribute("prevPage", form.getCurrentPage() - 1);
		}

		if (form.getCurrentPage() == totalPage) {
			model.addAttribute("nextPage", totalPage);
		} else {
			model.addAttribute("nextPage", form.getCurrentPage() + 1);
		}

		//スケージュールマップを返却
		return "user/management";
	}
}
