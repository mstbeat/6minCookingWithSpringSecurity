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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

	/**
	 * 商品情報一覧表示処理
	 * @param model モデル
	 * @return 商品情報一覧画面
	 */
	@GetMapping("/product-list")
	public String index(Model model) {
		List<Product> list = productService.findAll();
		model.addAttribute("listProducts", list);
		return "product-list";
	}

}
