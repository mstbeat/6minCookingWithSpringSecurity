package cooking.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cooking.entity.Authority;
import cooking.entity.User;
import cooking.entity.UserRegistrationForm;
import cooking.service.RegisterUserService;

@Controller
public class UserRegistrationController {

	@Autowired
	private RegisterUserService registerUserService;

	@RequestMapping("/registrationForm")
	public String showRegisterForm(Model model) {
		model.addAttribute(new UserRegistrationForm());
		return "registrationForm";
	}

	@RequestMapping("/register")
	public String registerUser(@ModelAttribute UserRegistrationForm userRegistrationForm) {
		User user = new User();

//		String encodedPassword = new BCryptPasswordEncoder().encode(userRegistrationForm.getPassword());
		user.setUsername(userRegistrationForm.getUsername());
		user.setPassword(userRegistrationForm.getPassword());
		
		Authority authority = new Authority();
		
		registerUserService.registerUser(user, authority);
		
		for (Long roleId : userRegistrationForm.getRoleId()) {
			authority.setRoleId(roleId);
			registerUserService.registerRole(user, authority);
		}

		return "result";
	}
}
