package cooking.app;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cooking.dao.ProductMapper;
import cooking.entity.GenreEnum;
import cooking.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	ProductMapper mapper;
	
	@GetMapping("/product-list")
	public ModelAndView index(Model model) {
		
		ModelAndView mav = new ModelAndView("ProductList");
		mav.addObject("listproducts", mapper.getAllProducts());
		return mav;

	}
	
	@RequestMapping("product-registration")
	public ModelAndView showForm() {
		ModelAndView mav = new ModelAndView("ProductRegistration");
		mav.addObject("product", new Product());
		mav.addObject("genreList", GenreEnum.values());
		return mav;
	}
	
	@PostMapping("/createProduct")
	public String createProduct(@ModelAttribute("product") Product product, 
			@RequestParam("multipartFile") MultipartFile multipartFile,
			@RequestParam("productImg") String productImg) throws IOException {

		if (product.getProductID() == 0){
			product.setProductImg(product.getMultipartFile().getBytes());
			mapper.createProduct(product);
		} else {
			if (multipartFile.isEmpty()) {
				product.setProductImg(DatatypeConverter.parseBase64Binary(productImg));
			} else {
				product.setProductImg(product.getMultipartFile().getBytes());
			}
			mapper.updateProduct(product);
		}
		return "redirect:/product-list";
	}
	
	@PostMapping("/product-edit")
	public  ModelAndView editProduct(@ModelAttribute("productID") int productID) {
		ModelAndView mav = new ModelAndView("ProductUpdate");
		Product product = mapper.findById(productID);
		mav.addObject("product", product);
		mav.addObject("productImg", product.getBase64Img());
		mav.addObject("genreList", GenreEnum.values());
		return mav;
	}
	
	@PostMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("productID") int productID) {
		mapper.deleteProduct(productID);
		return "redirect:/product-list";
	}
}
