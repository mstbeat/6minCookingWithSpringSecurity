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

/**
 * ジャンルを列挙型で扱い、値とキーを戻すenum.
 * @author Masato Yasuda
 */
public enum GenreEnum {

	/** 「ジャンル」指定なし。コード値は0です。 */
	NONE("0", "指定なし"),
	/** 「ジャンル」家電。コード値は1です。 */
	APPLIANCES("1", "家電"),
	/** 「ジャンル」家具。コード値は2です。 */
	FURNITURE("2", "家具"),
	/** 「ジャンル」食品。コード値は3です。 */
	FOOD("3", "食品"),
	/** 「ジャンル」ファッション。コード値は4です。 */
	FASHION("4", "ファッション"),
	/** 「ジャンル」書籍。コード値は5です。 */
	BOOK("5", "書籍");

	/** ジャンルの値 */
	private String value;

	/** ジャンルのキー */
	private String key;

	/**
	 * ジャンルの値とキーを基にジャンルを生成します。
	 * @param value ジャンルの値
	 * @param key ジャンルのキー
	 */
	private GenreEnum(String value , String key){
    	this.value = value;
    	this.key = key;
	}

	/**
	 * 値を得るメソッド.
	 * @return ジャンルの値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * キーを得るメソッド.
	 * @return ジャンルのキー
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * 値からキーを得るメソッド.
	 * @param value ジャンルの値
	 * @return ジャンルのキー
	 */
	public static String getKeyByValue(String value)
    {
        for(GenreEnum genre : values())
        {
            if(genre.getValue().equals(value))
            {
                return genre.getKey();
            }
        }
        throw new IllegalArgumentException(value);
    }
	
}
