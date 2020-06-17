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

package cooking.entity;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.DatatypeConverter;

import org.springframework.web.multipart.MultipartFile;

import cooking.validator.FileName;
import cooking.validator.FileSize;
import cooking.validator.FileType;

/**
 * 商品情報モデルを定義するクラス.
 * @author Masato Yasuda
 */
public class Product implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8789002951968301653L;

	/** 商品ID */
	private int productID;

	/** 商品名 */
	@NotBlank
	@Size(max = 25)
	private String productName;

	/** ジャンル */
	private String genre;

	/** メーカー */
	@NotBlank
	@Size(max = 20)
	private String maker;

	/** 販売価格 */
	@NotNull
	@Min(value = 1)
	@Max(value = 99999999)
	private BigDecimal sellingPrice;

	/** 商品説明 */
	@Size(max = 200)
	private String productDetail;

	/** 商品画像のbyte型 */
	private byte[] productImg;

	/** 削除フラグ */
	private String deleteFlg;

	/** 登録日時 */
	private Timestamp insertDate;

	/** 更新日時 */
	private Timestamp updateDate;

	/** 商品画像のString型 */
	private String stringImg;

	/** 商品画像のMultipartFile型 */
	@FileName(max = 15)
	@FileType
	@FileSize(max = 512000)
	private MultipartFile multipartFile;

	/**
	 * デフォルトコンストラクタ
	 */
	public Product() {

	}

	/**
	 * 商品IDを得るメソッド.
	 * @return 商品ID
	 */
	public int getProductID() {
		return productID;
	}

	/**
	 * 商品IDのセッターメソッド.
	 * @param productID 商品ID
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}

	/**
	 * 商品名を得るメソッド.
	 * @return 商品名
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * 商品名のセッターメソッド.
	 * @param productName 商品名
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * ジャンルを得るメソッド.
	 * @return ジャンル
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * ジャンルのセッターメソッド.
	 * @param genre ジャンル
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * メーカーを得るメソッド.
	 * @return メーカー
	 */
	public String getMaker() {
		return maker;
	}

	/**
	 * メーカーのセッターメソッド.
	 * @param maker メーカー
	 */
	public void setMaker(String maker) {
		this.maker = maker;
	}

	/**
	 * 販売価格を得るメソッド.
	 * @return 販売価格
	 */
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	/**
	 * 販売価格のセッターメソッド.
	 * @param sellingPrice 販売価格
	 */
	public void setSellingPrice(BigDecimal sellingPrice) {
		if (sellingPrice != null) {
			this.sellingPrice = sellingPrice.setScale(0, RoundingMode.HALF_UP);
		}
	}

	/**
	 * 商品説明を得るメソッド.
	 * @return 商品説明
	 */
	public String getProductDetail() {
		return productDetail;
	}

	/**
	 * 商品説明のセッターメソッド.
	 * @param productDetail 商品説明
	 */
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	/**
	 * 商品画像のMultipartFile型を得るメソッド.
	 * @return 商品画像のMultipartFile型
	 */
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	/**
	 * 商品画像のMultipartFile型のセッターメソッド.
	 * @param multipartFile 商品画像のMultipartFile型
	 */
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	/**
	 * 商品画像のbyte型を得るメソッド.
	 * @return 商品画像のbyte型
	 */
	public byte[] getProductImg() {
		return productImg;
	}

	/**
	 * 商品画像のbyte型のセッターメソッド.
	 * @param productImg 商品画像のbyte型
	 * @throws IOException 入出力例外が発生した場合
	 */
	public void setProductImg(byte[] productImg) throws IOException {
		this.productImg = productImg;
	}

	/**
	 * 削除フラグを得るメソッド.
	 * @return 削除フラグ
	 */
	public String getDeleteFlg() {
		return deleteFlg;
	}

	/**
	 * 削除フラグのセッターメソッド.
	 * @param deleteFlg 削除フラグ
	 */
	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

	/**
	 * 登録日時を得るメソッド.
	 * @return 登録日時
	 */
	public Timestamp getInsertDate() {
		return insertDate;
	}

	/**
	 * 登録日時のセッターメソッド.
	 * @param insertDate 登録日時
	 */
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * 更新日時を得るメソッド.
	 * @return 更新日時
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * 更新日時のセッターメソッド.
	 * @param updateDate 更新日時
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 商品画像のString型を得るメソッド.
	 * @return 商品画像のString型
	 */
	public String getStringImg() {
		stringImg = DatatypeConverter.printBase64Binary(this.productImg);
		return stringImg;
	}

	/**
	 * 商品画像のString型のセッターメソッド.
	 * @param stringImg 商品画像のString型
	 */
	public void setStringImg(String stringImg) {
		this.stringImg = stringImg;
	}

}
