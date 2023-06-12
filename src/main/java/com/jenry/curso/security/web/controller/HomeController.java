package com.jenry.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({"/","/home"})
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(ModelMap model){
		model.addAttribute("alerta","erro");
		model.addAttribute("titulo","Credenciais inválidas!");
		model.addAttribute("texto","Login ou senha inválidos, tente novamente");
		model.addAttribute("subtexto","Acesso permitido apenas para cadastros já ativados.");
		return "login";
	}

	@GetMapping("/acesso-negado")						//com response conseguimos pegar o status da requisição
	public String acessoNegado(ModelMap model, HttpServletResponse response){
		model.addAttribute("status", response.getStatus());
		model.addAttribute("error","Acesso negado");
		model.addAttribute("message","Você não tem permissão para esta área");
		return "error";
	}


}
