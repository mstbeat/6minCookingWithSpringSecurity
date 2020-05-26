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
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cooking.entity.GenreEnum;
import cooking.entity.Product;
import cooking.service.ProductService;

/**
 * 商品情報のコントローラ.
 * @author Masato Yasuda
 */
@Controller
public class ProductUpdateController {

	/** トランザクションを行うサービス */
	@Autowired
	ProductService productService;

	/** メッセージソース */
	@Autowired
	private MessageSource messageSource;

	/**
	 * バリデーションメッセージをメッセージソースに上書きするメソッド
	 * @return localValidatorFactoryBean メッセージソースに上書きしたバリデーター
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}

	/**
	 * 商品情報更新画面表示処理
	 * @param productID 商品ID
	 * @param model モデル
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @return 商品情報更新画面
	 */
	@GetMapping("/product-update")
	public String editProduct(@ModelAttribute("productID") int productID, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {
		String msg = (String) model.asMap().get("message");
		model.addAttribute("message", msg);
		Product product = productService.findOne(productID);
		if (product == null) {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG202", null, locale));
			return "redirect:/product-list";
		}
		if (!(product.getProductImg() == null)) {
			model.addAttribute("productImg", product.getBase64Img());
		}
		model.addAttribute("product", product);
		model.addAttribute("genreList", GenreEnum.values());
		return "ProductUpdate";
	}

	/**
	 * 商品情報更新処理
	 * @param product 商品情報
	 * @param bindingResult バインド結果
	 * @param model モデル
	 * @param multipartFile 商品画像のMultipartFile型
	 * @param productImg 商品画像のbyte型
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @throws IOException 入出力時に起こりえる例外
	 * @return 商品情報一覧
	 */
	@PostMapping("/product-update")
	public String updateProduct(@Valid @ModelAttribute("product") Product product,
			BindingResult bindingResult,
			Model model,
			@RequestParam("multipartFile") MultipartFile multipartFile,
			@RequestParam("productImg") Optional<String> productImg,
			RedirectAttributes redirectAttributes,
			Locale locale) throws IOException {

		if (multipartFile.isEmpty()) {
			if (productImg.isPresent()) {
				String img = productImg.get();
				product.setProductImg(DatatypeConverter.parseBase64Binary(img));
			}
		}
		if (!(multipartFile.isEmpty()) && !(bindingResult.hasFieldErrors("multipartFile"))) {
			product.setProductImg(product.getMultipartFile().getBytes());
			model.addAttribute("productImg", product.getBase64Img());
		}
		if (bindingResult.hasErrors()) {
			if (productImg.isPresent() && bindingResult.hasFieldErrors("multipartFile")) {
				String img = productImg.get();
				product.setProductImg(DatatypeConverter.parseBase64Binary(img));
			}
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductUpdate";
		}

		Integer count = productService.update(product);
		if (count == 0) {
			model.addAttribute("message", messageSource.getMessage("EMSG201", null, locale));
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductUpdate";
		} else {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
			model.addAttribute("product", product);
		}
		return "redirect:/product-list";
	}

	/**
	 * 商品情報削除処理
	 * @param productID 商品ID
	 * @param updateDate 更新日時
	 * @param product 商品情報
	 * @param model モデル
	 * @param bindingResult バインド結果
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @return 商品情報一覧
	 */
	@PostMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("productID") int productID,
			@ModelAttribute("updateDate") Timestamp updateDate,
			@ModelAttribute("product") Product product,
			Model model,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Locale locale) {
		Integer count = productService.delete(productID, updateDate);
		if (bindingResult.hasErrors()) {

		}
		if (count == 0) {
			model.addAttribute("message", messageSource.getMessage("EMSG202", null, locale));
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductUpdate";
		} else {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG203", null, locale));
		}
		return "redirect:/product-list";
	}

}
