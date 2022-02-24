package edu.shmonin.university.controller;

import edu.shmonin.university.exception.ServiceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ServiceException.class)
    public String getException(ServiceException e, Model model) {
        var message = String.format("%s %s", LocalDate.now(), e.getMessage());
        model.addAttribute("message", message);
        return "exceptions/exception";
    }
}