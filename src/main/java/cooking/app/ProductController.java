package cooking.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public String createProduct(@ModelAttribute("product") Product product) {
		
		if (product.getProductId() == 0){
			mapper.createProduct(product);
		} else {
			mapper.updateProduct(product);
		}
		return "redirect:/product-list";
	}
	
	@PostMapping("/product-edit")
	public  ModelAndView editProduct(@ModelAttribute("productId") int productId) {
		ModelAndView mav = new ModelAndView("ProductUpdate");
		Product product = mapper.findById(productId);
		mav.addObject("product", product);
		mav.addObject("genreList", GenreEnum.values());
		return mav;
	}
	
	@PostMapping("/product-delete")
	public String deleteProduct(@ModelAttribute("productId") int productId) {
		mapper.deleteProduct(productId);
		return "redirect:/product-list";
	}
}
