package cooking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

	private FileSize fs;
	
	@Override
	public void initialize(FileSize annotation) {
		this.fs = annotation;
	}
	
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (multipartFile.getSize() > 512000) {
			return false;
		}
		return true;
	}
}
