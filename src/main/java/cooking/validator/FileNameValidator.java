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

package cooking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

/**
 * 画像ファイル名桁数のバリデーション.
 * @author Masato Yasuda
 */
public class FileNameValidator implements ConstraintValidator<FileName, MultipartFile> {

	/** 最大桁数 */
	private int max;

	/**
	 * 初期化処理
	 * @param annotation アノテーション
	 */
	@Override
	public void initialize(FileName annotation) {
		max = annotation.max();
	}

	/**
	 * 商品画像のファイル名が15桁を超える場合はエラー
	 * @param multipartFile 商品画像のMultipartFile型
	 * @param context コンテキスト
	 * @return 制約を満たす場合はtrue、満たさない場合はfalse
	 */
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (!(multipartFile.isEmpty()) && multipartFile.getOriginalFilename().length() > max) {
			return false;
		}
		return true;
	}
}
