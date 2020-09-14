package cooking.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cooking.entity.Authority;
import cooking.entity.User;
import cooking.entity.UserRegistrationForm;
import cooking.service.RegisterUserService;
import cooking.service.UserServiceImpl;

@Controller
public class UserUpdateController {

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	@PostMapping("/user-update")
	public String editUser(@ModelAttribute("userId") Long userId, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {
		
		User user = userServiceImpl.findOne(userId);
		List<Integer> roleIds = new ArrayList<>();
		
		model.addAttribute("user", user);
		Collection<? extends GrantedAuthority> roles = user.getAuthorities();
		for (GrantedAuthority role : roles) {
			if (role.getAuthority().equals("ROLE_ADMIN")) {
				roleIds.add(1);;
			} else if (role.getAuthority().equals("ROLE_MANAGER")) {
				roleIds.add(2);
			} else if (role.getAuthority().equals("ROLE_EMPLOYEE")) {
				roleIds.add(3);
			}
		}
		model.addAttribute("roleIds", roleIds);
		return "userUpdate";
		
	}
	
	@PutMapping("/user-update")
	public String updateUser(@Validated @ModelAttribute("form") UserRegistrationForm userRegistrationForm,
			@ModelAttribute("user") User user,
			BindingResult bindingResult) {
		
		Authority authority = new Authority();
		
		user.setUsername(userRegistrationForm.getUsername());
		user.setPassword(userRegistrationForm.getPassword());
		
		for (Long roleId : userRegistrationForm.getRoleId()) {
			authority.setRoleId(roleId);
			registerUserService.registerRole(user, authority);
		}
		
		if (bindingResult.hasErrors()) {
			return "userUpdate";
		}
		
		registerUserService.update(user);
		return "redirect:/user-list";
	}
}
