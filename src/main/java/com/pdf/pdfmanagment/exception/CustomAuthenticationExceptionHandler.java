package com.pdf.pdfmanagment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomAuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotVerifiedException.class)
    public ModelAndView handleUserNotVerifiedException(UserNotVerifiedException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setViewName("login"); // Customize the view name according to your project setup
        return modelAndView;
    }

    // Other exception handlers...

}
