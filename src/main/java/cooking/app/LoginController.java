package cooking.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ログイン認証のコントローラ.
 * @author Masato Yasuda
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面表示処理
	 * @return ログイン画面
	 */
	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {
		return "login";
	}

	/**
	 * アクセス権限エラー画面表示処理
	 * @return アクセス権限エラー画面
	 */
	@RequestMapping("/toError")
	public String toError() {
		return "accessDenied";
	}
}
