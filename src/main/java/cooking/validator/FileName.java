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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 画像ファイル名桁数のバリデーションのアノテーション設定.
 * @author Masato Yasuda
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileNameValidator.class)
@Documented
public @interface FileName {
	String message() default "{EMSG101}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int max();
}
