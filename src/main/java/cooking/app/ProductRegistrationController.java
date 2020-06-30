/**
 * Copyright (c) Proud Data Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details. *
 * THE SOFTWARE IS PROVIDED BY Proud Group
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. */

package cooking.app;

import java.io.IOException;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cooking.entity.Product;
import cooking.service.ProductService;

/**
 * 商品情報登録のコントローラ.
 * @author Masato Yasuda
 */
@Controller
public class ProductRegistrationController {

	/** トランザクションを行うサービス */
	@Autowired
	ProductService productService;

	/** メッセージソース */
	@Autowired
	private MessageSource messageSource;

	/**
	 * 商品情報登録画面表示処理
	 * @param model モデル
	 * @return 商品情報登録画面
	 */
	@GetMapping("/product-registration")
	public String showForm(Model model) {
		model.addAttribute("product", new Product());
		return "productregistration";
	}

	/**
	 * 商品情報登録処理
	 * @param product 商品情報
	 * @param bindingResult バインド結果
	 * @param model モデル
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @throws IOException 入出力時に起こりえる例外
	 * @return 登録が完了した場合は商品情報一覧画面、入力チェックにてtrueの場合は商品情報登録画面
	 */
	@PostMapping("/product-registration")
	public String createProduct(@Valid @ModelAttribute("product") Product product,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			Locale locale) throws IOException {

		if (bindingResult.hasErrors()) {
			return "productregistration";
		}
		if (!product.getMultipartFile().isEmpty()) {
			product.setProductImg(product.getMultipartFile().getBytes());
		}
		productService.save(product);
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG201", null, locale));
		return "redirect:/product-list";
	}
}
