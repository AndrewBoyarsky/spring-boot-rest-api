package com.hesky.test.web;

import com.hesky.test.util.exception.NotFoundException;
import com.hesky.test.util.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handler for Rest Controllers
 */
@ControllerAdvice(annotations = {RestController.class})
public class ExceptionInfoHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorInfo handleNotFoundException(HttpServletRequest request, Exception ex) {
        return new ErrorInfo(request.getRequestURL(), ex.getClass().getSimpleName(), ex.getMessage());
    }
}
