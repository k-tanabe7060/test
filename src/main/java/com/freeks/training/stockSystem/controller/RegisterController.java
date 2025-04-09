package com.freeks.training.stockSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.freeks.training.stockSystem.entity.LoginInfoDto;
import com.freeks.training.stockSystem.entity.LoginInfoEntity;
import com.freeks.training.stockSystem.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	//registerというURLに対するgetリクエストを処理
	@GetMapping("/register")
	public ModelAndView registerForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", new LoginInfoDto());
		mav.setViewName("register");
		return mav;
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute LoginInfoDto loginInfoDto) {
		LoginInfoEntity exist = userService.findByUserName(null);
		if(exist != null) {
			return "register";
		}
		userService.save(loginInfoDto);
		return "login";
	}
	
	
}
