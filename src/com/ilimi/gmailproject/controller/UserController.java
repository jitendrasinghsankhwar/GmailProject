package com.ilimi.gmailproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ilimi.gmailproject.bean.UserBean;

@Controller
public class UserController {
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void saveUserInfo(@ModelAttribute UserBean user){
		
	}
	
}
