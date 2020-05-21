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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import cooking.entity.Product;
import cooking.service.ProductService;

/**
 * 商品情報のコントローラ.
 * @author Masato Yasuda
 */
@Controller
public class ProductListController {

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
	 * 商品情報一覧表示処理
	 * @param model モデル
	 * @return 商品情報一覧画面
	 */
	@GetMapping("/product-list")
	public ModelAndView index(Model model) {
		String msg = (String) model.asMap().get("message");
		model.addAttribute("message", msg);
		ModelAndView mav = new ModelAndView("ProductList");
		List<Product> list = productService.findAll();
		mav.addObject("listproducts", list );
		return mav;
	}

}
