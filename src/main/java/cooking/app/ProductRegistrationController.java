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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cooking.entity.GenreEnum;
import cooking.entity.Product;
import cooking.service.ProductService;

/**
 * 商品情報のコントローラ.
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
	 * 商品情報登録画面表示処理
	 * @return 商品情報登録画面
	 */
	@GetMapping("/product-registration")
	public ModelAndView showForm() {
		ModelAndView mav = new ModelAndView("ProductRegistration");
		mav.addObject("product", new Product());
		mav.addObject("genreList", GenreEnum.values());
		return mav;
	}

	/**
	 * 商品情報登録処理
	 * @param product 商品情報
	 * @param bindingResult バインド結果
	 * @param model モデル
	 * @param multipartFile 商品画像のMultipartFile型
	 * @param productImg 商品画像のbyte型
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @throws IOException 入出力例外が発生した場合
	 * @return 商品情報一覧
	 */
	@PostMapping("/product-registration")
	public String createProduct(@Valid @ModelAttribute("product") Product product,
			BindingResult bindingResult,
			Model model,
			@RequestParam("multipartFile") MultipartFile multipartFile,
			@RequestParam("productImg") Optional<String> productImg,
			RedirectAttributes redirectAttributes,
			Locale locale) throws IOException {

		if (!(multipartFile.isEmpty()) && !(bindingResult.hasFieldErrors("multipartFile"))) {
			product.setProductImg(product.getMultipartFile().getBytes());
		}
		if (bindingResult.hasErrors()) {
			if (!(multipartFile.isEmpty()) && !(bindingResult.hasFieldErrors("multipartFile"))) {
				product.setProductImg(product.getMultipartFile().getBytes());
			}
			if (multipartFile.isEmpty() && productImg.isPresent()) {
				String img = productImg.get();
				product.setProductImg(DatatypeConverter.parseBase64Binary(img));
				model.addAttribute("productImg", product.getBase64Img());
			}
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductRegistration";
		}
		if (productImg.isPresent()) {
			String img = productImg.get();
			product.setProductImg(DatatypeConverter.parseBase64Binary(img));
			model.addAttribute("productImg", product.getBase64Img());
		}
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG201", null, locale));
		productService.save(product);
		model.addAttribute("product", product);
		return "redirect:/product-list";
	}
}
