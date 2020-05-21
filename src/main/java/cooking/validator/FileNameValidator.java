package cooking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileNameValidator implements ConstraintValidator<FileName, MultipartFile> {

	private FileName fn;
	
	@Override
	public void initialize(FileName annotation) {
		this.fn = annotation;
	}
	
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (multipartFile.getOriginalFilename().length() >= 15) {
			return false;
		}
		return true;
	}
}
