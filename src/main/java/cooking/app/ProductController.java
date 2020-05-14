package cooking.app;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
			RedirectAttributes redirectAttributes) throws IOException {
		
		if (!(multipartFile.isEmpty())) {
			if (!(multipartFile.getContentType().toLowerCase().equals("image/jpg") || 
					multipartFile.getContentType().toLowerCase().equals("image/jpeg") || 
					multipartFile.getContentType().toLowerCase().equals("image/png"))) {
				bindingResult.rejectValue("multipartFile", null, "ファイル形式が「JPEG」、「PNG」以外の画像は登録できません。");
			}
			if (multipartFile.getOriginalFilename().length() >= 15 ){
				bindingResult.rejectValue("multipartFile", null, "ファイル名(拡張子含む)が15桁を超える画像は登録できません。");
			}
			if (multipartFile.getSize() > 500000) {
				bindingResult.rejectValue("multipartFile", null, "ファイルサイズが500KBを超える画像は登録できません。");
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
		
		redirectAttributes.addFlashAttribute("message", "商品情報を登録しました。");
		mapper.createProduct(product);
		
		model.addAttribute("product", product);
		return "redirect:/product-list";
	}
	
	@GetMapping("/product-update")
	public  String editProduct(@ModelAttribute("productID") int productID, Model model, RedirectAttributes redirectAttributes) {
		String msg = (String) model.asMap().get("message");
		model.addAttribute("message", msg);
		Product product = mapper.findById(productID);
		if (product == null) {
			redirectAttributes.addFlashAttribute("message","選択された情報は既に他ユーザーにより削除されています。");
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
			RedirectAttributes redirectAttributes) throws IOException {
		
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
				bindingResult.rejectValue("multipartFile", null, "ファイル形式が「JPEG」、「PNG」以外の画像は登録できません。");
			}
			if (multipartFile.getOriginalFilename().length() >= 15 ){
				bindingResult.rejectValue("multipartFile", null, "ファイル名(拡張子含む)が15桁を超える画像は登録できません。");
			}
			if (multipartFile.getSize() > 500000) {
				bindingResult.rejectValue("multipartFile", null, "ファイルサイズが500KBを超える画像は登録できません。");
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
					redirectAttributes.addFlashAttribute("message", "選択された情報は既に他ユーザーにより更新されています。");
					redirectAttributes.addAttribute("productID", product.getProductID());
					return "redirect:/product-update";
				}
				if (product.getProductImg() == null || beforeProduct.getProductImg() == null){
					redirectAttributes.addFlashAttribute("message", "商品情報を更新しました。");
					mapper.updateProduct(product);
					model.addAttribute("product", product);
					return "redirect:/product-list";
				}
				if (product.getBase64Img().equals(beforeProduct.getBase64Img())) {
					redirectAttributes.addFlashAttribute("message", "選択された情報は既に他ユーザーにより更新されています。");
					redirectAttributes.addAttribute("productID", product.getProductID());
					return "redirect:/product-update";
				}
			}
			if (product.getProductDetail() == null || beforeProduct.getProductDetail() == null) {
				redirectAttributes.addFlashAttribute("message", "商品情報を更新しました。");
				mapper.updateProduct(product);
				model.addAttribute("product", product);
				return "redirect:/product-list";
			} else {
				if (product.getProductDetail().equals(beforeProduct.getProductDetail())) {
					if (product.getProductImg() == null && beforeProduct.getProductImg() == null) {
						redirectAttributes.addFlashAttribute("message", "選択された情報は既に他ユーザーにより更新されています。");
						redirectAttributes.addAttribute("productID", product.getProductID());
						return "redirect:/product-update";
					}
					if (product.getProductImg() == null || beforeProduct.getProductImg() == null){
						redirectAttributes.addFlashAttribute("message", "商品情報を更新しました。");
						mapper.updateProduct(product);
						model.addAttribute("product", product);
						return "redirect:/product-list";
					}
					if (product.getBase64Img().equals(beforeProduct.getBase64Img())) {
						redirectAttributes.addFlashAttribute("message", "選択された情報は既に他ユーザーにより更新されています。");
						redirectAttributes.addAttribute("productID", product.getProductID());
						return "redirect:/product-update";
					}
				}
			}
		}
		redirectAttributes.addFlashAttribute("message", "商品情報を更新しました。");
		mapper.updateProduct(product);

		model.addAttribute("product", product);
		return "redirect:/product-list";
	}
	
	@PostMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("productID") int productID, Model model, RedirectAttributes redirectAttributes) {
		mapper.deleteProduct(productID);
		redirectAttributes.addFlashAttribute("message", "商品情報を削除しました。");
		return "redirect:/product-list";
	}
	
}
