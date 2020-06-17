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
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	 * 商品情報更新画面表示処理
	 * @param productID 商品ID
	 * @param model モデル
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @return 商品情報更新画面
	 */
	@PostMapping("/product-update")
	public String editProduct(@ModelAttribute("productID") int productID, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {

		Product product = productService.findOne(productID);
		if (product == null) {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG202", null, locale));
			return "redirect:/product-list";
		}

		model.addAttribute("product", product);
		return "update";
	}

	/**
	 * 商品情報更新処理
	 * @param product 商品情報
	 * @param bindingResult バインド結果
	 * @param model モデル
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @throws IOException 入出力時に起こりえる例外
	 * @return 商品情報一覧画面
	 */
	@PutMapping("/product-update")
	public String updateProduct(@Valid @ModelAttribute("product") Product product,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes,
			Locale locale) throws IOException {

		Product dbProduct = productService.findOne(product.getProductID());

		if (bindingResult.hasErrors()) {
			if (dbProduct.getProductImg() != null) {
				product.setProductImg(DatatypeConverter.parseBase64Binary(dbProduct.getStringImg()));
			}
			return "update";
		}

		if (!product.getMultipartFile().isEmpty()) {
			product.setProductImg(product.getMultipartFile().getBytes());
			model.addAttribute("productImg", product.getStringImg());
		} else if (dbProduct.getProductImg() != null) {
			product.setProductImg(DatatypeConverter.parseBase64Binary(dbProduct.getStringImg()));
		}

		Integer count = productService.update(product);
		if (count == 0) {
			model.addAttribute("message", messageSource.getMessage("EMSG201", null, locale));
			return "update";
		} else {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
		}
		return "redirect:/product-list";
	}

	/**
	 * 商品情報削除処理
	 * @param product 商品情報
	 * @param model モデル
	 * @param redirectAttributes リダイレクト時の情報受け渡し
	 * @param locale 実行環境のロケール
	 * @return 商品情報一覧画面
	 */
	@DeleteMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("product") Product product,
			Model model,
			RedirectAttributes redirectAttributes, Locale locale) {

		Integer count = productService.delete(product);
		if (count == 0) {
			model.addAttribute("message", messageSource.getMessage("EMSG202", null, locale));
			model.addAttribute("product", product);
			return "update";
		} else {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG203", null, locale));
		}
		return "redirect:/product-list";
	}

}
