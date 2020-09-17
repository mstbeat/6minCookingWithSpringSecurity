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
		List<Long> roleIds = new ArrayList<>();

		model.addAttribute("user", user);
		Collection<? extends GrantedAuthority> roles = user.getAuthorities();
		for (GrantedAuthority role : roles) {
			if (role.getAuthority().equals("ROLE_ADMIN")) {
				roleIds.add((long) 1);
			} else if (role.getAuthority().equals("ROLE_MANAGER")) {
				roleIds.add((long) 2);
			} else if (role.getAuthority().equals("ROLE_EMPLOYEE")) {
				roleIds.add((long) 3);
			}
		}

		UserRegistrationForm form = new UserRegistrationForm();
		Long[] ids = roleIds.toArray(new Long[roleIds.size()]);
		form.setRoleId(ids);

		model.addAttribute("form", form);
		model.addAttribute("roleIds", roleIds);
		return "userUpdate";

	}

	@PutMapping("/user-update")
	public String updateUser(@Validated @ModelAttribute("form") UserRegistrationForm userRegistrationForm,
			BindingResult bindingResult,
			@ModelAttribute("user") User user,
			Model model) {

		if (bindingResult.hasErrors()) {
			List<Long> roleIds = new ArrayList<>();
			UserRegistrationForm form = new UserRegistrationForm();
			Long[] ids = roleIds.toArray(new Long[roleIds.size()]);
			form.setRoleId(ids);
			model.addAttribute("roleIds", roleIds);
			return "userUpdate";
		}

		Authority authority = new Authority();

		user.setUsername(userRegistrationForm.getUsername());
		user.setPassword(userRegistrationForm.getPassword());

		registerUserService.update(user);

		registerUserService.deleteRole(user);
		for (Long roleId : userRegistrationForm.getRoleId()) {
			authority.setRoleId(roleId);
			registerUserService.updateRole(user, authority);
		}

		return "redirect:/user-list";
	}
}
