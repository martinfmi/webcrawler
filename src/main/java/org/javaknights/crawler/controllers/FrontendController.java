package org.javaknights.crawler.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

	@RequestMapping("/home")
	public ModelAndView home(ModelAndView view,
			@RequestParam(required = false) String text) {
		view.setViewName("home");
//		view.addObject("text", "Text from injected ModelAndView object !");
		view.addObject("color", "green");
		return view;
	}
	
	@RequestMapping("/text")
	public ResponseEntity<String> text() {
		return ResponseEntity.ok().body("Text for Ajax call !");
	}
	
	@ModelAttribute("text")
	public List<String> getTextAttribute() {
		return Arrays.asList("first", "second", "third");
	}
	
}
