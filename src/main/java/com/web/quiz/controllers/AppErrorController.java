package com.web.quiz.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    @RequestMapping({"/error"})
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "Internal server error";
        String typeError = message;
        int error = 500;
        if (status != null) {
            error = Integer.parseInt(status.toString());
            if(error == HttpStatus.NOT_FOUND.value()) {
                message = "Request page not found";
                typeError = "Not found";
            } else if(error == HttpStatus.FORBIDDEN.value()) {
                message = "You don't have permission";
                typeError = "Forbidden";
            }
        }
        model.addAttribute("status", error);
        model.addAttribute("message", message);
        model.addAttribute("typeError", typeError);
        return "error";
    }

    @RequestMapping({"/error/{error}"})
    public String handleError(HttpServletRequest request, Model model, @PathVariable Integer error) {
        if (error == null) {
            error = 500;
        }
        String message = "Internal server error";
        String typeError = message;
        if(error == HttpStatus.NOT_FOUND.value()) {
            message = "Request page not found";
            typeError = "Not found";
        } else if(error == HttpStatus.FORBIDDEN.value()) {
            message = "You don't have permission";
            typeError = "Forbidden";
        }
        model.addAttribute("status", error);
        model.addAttribute("message", message);
        model.addAttribute("typeError", typeError);
        return "error";
    }
}
