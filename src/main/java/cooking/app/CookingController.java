package cooking.app;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookingController {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public CookingController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@GetMapping("/")
	public String showList(Model model) {
		String sql = "SELECT genre, maker, productName, sellingPrice FROM t_ProductInfo WHERE productId=11";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		model.addAttribute("productId", map.get("productId"));
		model.addAttribute("genre", map.get("genre"));
		model.addAttribute("maker", map.get("maker"));
		model.addAttribute("productName", map.get("productName"));
		model.addAttribute("sellingPrice", map.get("sellingPrice"));
		return "list";
	}
		
	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("title", "商品登録");
		return "registration";
	}
	
	
	@GetMapping("/update")
	public String update(Model model) {
		return "update";
	}
	
	@GetMapping("/delete")
	public String delete(Model model) {
		return "update";
	}
}
