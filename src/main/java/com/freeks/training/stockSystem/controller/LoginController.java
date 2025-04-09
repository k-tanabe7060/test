package com.freeks.training.stockSystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping(value = "/login")
	public String login() {
		return "login"; //login.htmlを返却
	}
	
	@GetMapping(value = "/home")
	public String redirectToIndex() {
		//現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 現在のユーザーの認証情報を取得します
        if(authentication != null && authentication.isAuthenticated()) {
        	return "redirect:/home";
        }
        return "redirect:/login";
	}
	
	@GetMapping(value = "/index")
	public String index() {
		return "home";
	}
}
