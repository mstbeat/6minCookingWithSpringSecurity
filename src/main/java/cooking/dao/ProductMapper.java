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

package cooking.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import cooking.entity.Product;
import cooking.util.MyBatisUtil;

/**
 * 商品情報のマッパー.
 * @author Masato Yasuda
 */
@Repository
public class ProductMapper {

	/**
	 * 全ての商品情報を取得するメソッド
	 * @return 全ての商品情報
	 */
	public List<Product> getAllProducts() {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		List<Product> productList = session.selectList("getProductInfoList");
		session.commit();
		session.close();
		return productList;
	}

	/**
	 * 商品情報を登録するメソッド
	 * @param product 商品情報
	 */
	public void createProduct(Product product) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.insert("insertProductInfo", product);
		session.commit();
		session.close();
	}

	/**
	 * 商品IDで商品情報を取得するメソッド
	 * @param productID 商品ID
	 * @return 商品情報
	 */
	public Product findById(int productID) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		Product product = (Product) session.selectOne("getProductInfo", productID);
		session.commit();
		session.close();
		return product;
	}

	/**
	 * 商品情報を更新するメソッド
	 * @param product 商品情報
	 */
	public void updateProduct(Product product) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.update("updateProductInfo", product);
		session.commit();
		session.close();
	}

	/**
	 * 商品情報を削除するメソッド
	 * @param productID 商品ID
	 */
	public void deleteProduct(int productID) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.delete("deleteProductInfo", productID);
		session.commit();
		session.close();
	}
}
