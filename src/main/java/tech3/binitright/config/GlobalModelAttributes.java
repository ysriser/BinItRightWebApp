package tech3.binitright.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public final class GlobalModelAttributes {

    @ModelAttribute("currentPath")
    public String currentPath(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}