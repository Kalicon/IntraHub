package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Página Não Encontrada");
        model.addAttribute("message", "O recurso solicitado não existe ou foi movido.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", "Erro Interno do Servidor");
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return "error";
    }
}