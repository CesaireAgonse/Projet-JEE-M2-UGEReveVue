package fr.uge.revevue.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;



//@Controller
class ErrorTypeController implements ErrorController {

    @GetMapping("/error")
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,
                              Model model){
        Object status = null;
        Object errorType = null;
        status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        errorType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("status", statusCode);
            //model.addAttribute("errorType", errorType.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error-404";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errors/error-403";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                Object error = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
                model.addAttribute("errorMessage", error != "" ? error.toString() : "Unknown Error");
                return "errors/error-default";
            }
        }
        return "errors/error-default";
    }

}