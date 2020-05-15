package cooking.app;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cooking.dao.ProductMapper;
import cooking.entity.GenreEnum;
import cooking.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	ProductMapper mapper;
	
	@Autowired
	private MessageSource messageSource;
	
	@Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    public org.springframework.validation.Validator getValidator() {
        return validator();
    }

	@GetMapping("/product-list")
	public ModelAndView index(Model model) {
		String msg = (String) model.asMap().get("message");
		model.addAttribute("message", msg);
		ModelAndView mav = new ModelAndView("ProductList");
		mav.addObject("listproducts", mapper.getAllProducts());
		return mav;

	}
	
	@GetMapping("/product-registration")
	public ModelAndView showForm() {
		ModelAndView mav = new ModelAndView("ProductRegistration");
		mav.addObject("product", new Product());
		mav.addObject("genreList", GenreEnum.values());
		return mav;
	}
	
	@PostMapping("/product-registration")
	public String createProduct(@Valid @ModelAttribute("product") Product product, 
			BindingResult bindingResult, 
			Model model, 
			@RequestParam("multipartFile") MultipartFile multipartFile,
			@RequestParam("productImg") Optional<String> productImg, 
			RedirectAttributes redirectAttributes, 
			Locale locale) throws IOException {
		
		;
		
		if (!(multipartFile.isEmpty())) {
			if (!(multipartFile.getContentType().toLowerCase().equals("image/jpg") || 
					multipartFile.getContentType().toLowerCase().equals("image/jpeg") || 
					multipartFile.getContentType().toLowerCase().equals("image/png"))) {
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG102", null, locale));
			}
			if (multipartFile.getOriginalFilename().length() >= 15 ){
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG101", null, locale));
			}
			if (multipartFile.getSize() > 500000) {
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG103", null, locale));
			}
		}
		
		if (bindingResult.hasErrors()) {
			if (!(multipartFile.isEmpty())) {
				product.setProductImg(product.getMultipartFile().getBytes());
			}
			if (productImg.isPresent()) {
				String img = productImg.get();
				product.setProductImg(DatatypeConverter.parseBase64Binary(img));
				model.addAttribute("productImg", product.getBase64Img());
			}
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductRegistration";
		}
		if (productImg.isPresent()) {
			String img = productImg.get();
			product.setProductImg(DatatypeConverter.parseBase64Binary(img));
		}
		
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG201", null, locale));
		mapper.createProduct(product);
		
		model.addAttribute("product", product);
		return "redirect:/product-list";
	}
	
	@GetMapping("/product-update")
	public  String editProduct(@ModelAttribute("productID") int productID, Model model, RedirectAttributes redirectAttributes, Locale locale) {
		String msg = (String) model.asMap().get("message");
		model.addAttribute("message", msg);
		Product product = mapper.findById(productID);
		if (product == null) {
			redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG202", null, locale));
			return "redirect:/product-list";
		}
		if (!(product.getProductImg() == null)) {
			model.addAttribute("productImg", product.getBase64Img());
		}
		model.addAttribute("product", product);
		model.addAttribute("genreList", GenreEnum.values());
		return "ProductUpdate";
	}
	
	@PostMapping("/product-update")
	public String updateProduct(@Valid @ModelAttribute("product") Product product, 
			BindingResult bindingResult, 
			Model model, 
			@RequestParam("multipartFile") MultipartFile multipartFile,
			@RequestParam("productImg") Optional<String> productImg, 
			RedirectAttributes redirectAttributes, 
			Locale locale) throws IOException {
		
		Product beforeProduct = mapper.findById(product.getProductID());
		
		
		if (multipartFile.isEmpty()) {
			if (productImg.isPresent()) {
				String img = productImg.get();
				product.setProductImg(DatatypeConverter.parseBase64Binary(img));
			}
		} else {
			if (!(multipartFile.getContentType().toLowerCase().equals("image/jpg") || 
					multipartFile.getContentType().toLowerCase().equals("image/jpeg") || 
					multipartFile.getContentType().toLowerCase().equals("image/png"))) {
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG102", null, locale));
			}
			if (multipartFile.getOriginalFilename().length() >= 15 ){
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG101", null, locale));
			}
			if (multipartFile.getSize() > 500000) {
				bindingResult.rejectValue("multipartFile", null, messageSource.getMessage("EMSG103", null, locale));
			}
			product.setProductImg(product.getMultipartFile().getBytes());
		}
		if (bindingResult.hasErrors()) {
			if (productImg.isPresent()) {
				model.addAttribute("productImg", product.getBase64Img());
			}
			model.addAttribute("product", product);
			model.addAttribute("genreList", GenreEnum.values());
			return "ProductUpdate";
		}
		if (product.getProductName().equals(beforeProduct.getProductName()) && 
				product.getGenre().equals(beforeProduct.getGenre()) &&
				product.getMaker().equals(beforeProduct.getMaker()) &&
				product.getSellingPrice().equals(beforeProduct.getSellingPrice())) {
			if (product.getProductDetail() == null && beforeProduct.getProductDetail() == null) {
				if (product.getProductImg() == null && beforeProduct.getProductImg() == null) {
					redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG201", null, locale));
					redirectAttributes.addAttribute("productID", product.getProductID());
					return "redirect:/product-update";
				}
				if (product.getProductImg() == null || beforeProduct.getProductImg() == null){
					redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
					mapper.updateProduct(product);
					model.addAttribute("product", product);
					return "redirect:/product-list";
				}
				if (product.getBase64Img().equals(beforeProduct.getBase64Img())) {
					redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG201", null, locale));
					redirectAttributes.addAttribute("productID", product.getProductID());
					return "redirect:/product-update";
				}
			}
			if (product.getProductDetail() == null || beforeProduct.getProductDetail() == null) {
				redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
				mapper.updateProduct(product);
				model.addAttribute("product", product);
				return "redirect:/product-list";
			} else {
				if (product.getProductDetail().equals(beforeProduct.getProductDetail())) {
					if (product.getProductImg() == null && beforeProduct.getProductImg() == null) {
						redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG201", null, locale));
						redirectAttributes.addAttribute("productID", product.getProductID());
						return "redirect:/product-update";
					}
					if (product.getProductImg() == null || beforeProduct.getProductImg() == null){
						redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
						mapper.updateProduct(product);
						model.addAttribute("product", product);
						return "redirect:/product-list";
					}
					if (product.getBase64Img().equals(beforeProduct.getBase64Img())) {
						redirectAttributes.addFlashAttribute("message", messageSource.getMessage("EMSG201", null, locale));
						redirectAttributes.addAttribute("productID", product.getProductID());
						return "redirect:/product-update";
					}
				}
			}
		}
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG202", null, locale));
		mapper.updateProduct(product);

		model.addAttribute("product", product);
		return "redirect:/product-list";
	}
	
	@PostMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("productID") int productID, Model model, RedirectAttributes redirectAttributes, Locale locale) {
		mapper.deleteProduct(productID);
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("IMSG203", null, locale));
		return "redirect:/product-list";
	}
	
}
