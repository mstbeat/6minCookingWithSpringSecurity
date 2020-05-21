package cooking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

	private FileType ft;
	
	@Override
	public void initialize(FileType annotation) {
		this.ft = annotation;
	}
	
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (!(multipartFile.isEmpty())) {
			if (!(multipartFile.getContentType().toLowerCase().equals("image/jpg") ||
					multipartFile.getContentType().toLowerCase().equals("image/jpeg") ||
					multipartFile.getContentType().toLowerCase().equals("image/png"))) {
				return false;
			}
		}
		return true;
	}
}
