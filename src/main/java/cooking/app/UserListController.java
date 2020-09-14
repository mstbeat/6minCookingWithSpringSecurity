package cooking.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cooking.entity.User;
import cooking.service.UserServiceImpl;

@Controller
public class UserListController {

	@Autowired
	UserServiceImpl userServiceImpl;

	@GetMapping("/user-list")
	public String index(Model model) {
		List<User> users = userServiceImpl.findAll();
		model.addAttribute("listUsers", users);
		return "userList";
	}

}
