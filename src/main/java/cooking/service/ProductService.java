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

package cooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cooking.entity.Product;
import cooking.repository.ProductMapper;

/**
 * 商品情報のトランザクションを行うサービス.
 * @author Masato Yasuda
 */
@Service
public class ProductService {

	/** トランザクションを行うマッパー */
	@Autowired
	ProductMapper productMapper;

	/**
	 * 全ての商品情報を取得するメソッド
	 * @return 全ての商品情報
	 */
	public List<Product> findAll() {
		return productMapper.getProductInfoList();
	}

	/**
	 * 商品情報を登録するメソッド
	 * @param product 商品情報
	 */
	@Transactional
	public void save(Product product) {
		productMapper.insertProductInfo(product);
	}

	/**
	 * 商品IDで商品情報を取得するメソッド
	 * @param productID 商品ID
	 * @return 商品情報
	 */
	public Product findOne(int productID) {
		return productMapper.getProductInfo(productID);
	}

	/**
	 * 商品情報を更新するメソッド
	 * @param product 商品情報
	 * @return 商品更新件数
	 */
	@Transactional
	public int update(Product product) {
		return productMapper.updateProductInfo(product);
	}

	/**
	 * 商品情報を削除するメソッド
	 * @param product 商品情報
	 * @return 商品削除件数
	 */
	@Transactional
	public int delete(Product product) {
		return productMapper.deleteProductInfo(product);
	}
}
