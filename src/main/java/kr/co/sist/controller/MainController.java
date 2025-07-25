package kr.co.sist.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value = "/", method = { GET, POST })
	public String index(Model model) {

		model.addAttribute("helloMsg", "안뇽! 타임리프!");
		model.addAttribute("helloMsg2", "<strong>안뇽! 타임리프!</strong>");// 태그가 그대로 실행된다=XSS방지 가능
		model.addAttribute("name", "이장훈");
		model.addAttribute("name2", "현석주");

		model.addAttribute("id", "ju");

		return "index";// /templates/ , .html
	}// index

}// class