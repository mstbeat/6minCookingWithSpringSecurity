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
 * 画像ファイルタイプのバリデーションのアノテーション設定.
 * @author Masato Yasuda
 */
public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

	/**
	 * 初期化処理
	 * @param annotation アノテーション
	 */
	@Override
	public void initialize(FileType annotation) {
	}

	/**
	 * 商品画像のファイル形式が「JPEG」、「PNG」以外はエラー
	 * @param multipartFile 商品画像のMultipartFile型
	 * @param context コンテキスト
	 * @return 制約を満たす場合はtrue、満たさない場合はfalse
	 */
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (!(multipartFile.isEmpty())) {
			if (multipartFile.getContentType().equals("image/jpeg") ||
					multipartFile.getContentType().equals("image/png")) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
}
