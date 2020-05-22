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

package cooking.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cooking.entity.Product;

/**
 * 商品情報のマッパー.
 * @author Masato Yasuda
 */
@Mapper
public interface ProductMapper {

	/**
	 * 全ての商品情報を取得するメソッド
	 * @return 全ての商品情報
	 */
	List<Product> getProductInfoList();

	/**
	 * 商品情報を登録するメソッド
	 * @param product 商品情報
	 */
	void createProduct(Product product);

	/**
	 * 商品IDで商品情報を取得するメソッド
	 * @param productID 商品ID
	 * @return 商品情報
	 */
	Product findById(int productID);

	/**
	 * 商品情報を更新するメソッド
	 * @param product 商品情報
	 * @return 商品更新件数
	 */
	int updateProduct(Product product);

	/**
	 * 商品情報を削除するメソッド
	 * @param productID 商品ID
	 * @param updateDate 更新日時
	 * @return 商品削除件数
	 */
	int deleteProduct(@Param("productID") int productID, @Param("updateDate") Timestamp updateDate);
}
