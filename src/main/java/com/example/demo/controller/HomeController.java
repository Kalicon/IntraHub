package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <--- Atenção: É @Controller, NÃO @RestController
public class HomeController {

    // Quando acessar "localhost:8080/login", mostra o arquivo login.html
    @GetMapping("/login")
    public String login() {
        return "login"; // O Spring vai procurar por "templates/login.html"
    }

    // Quando acessar "localhost:8080/", mostra o arquivo index.html (Home)
    @GetMapping("/")
    public String home() {
        return "index"; // O Spring vai procurar por "templates/index.html"
    }
}