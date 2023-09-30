package com.mk.BackendQuiz.exception;

import com.mk.BackendQuiz.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExceptionManager.EntityNotFoundException.class)
    public final ResponseEntity handleNotFoundExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExceptionManager.EntityException.class)
    public final ResponseEntity handleEntityException(Exception ex, WebRequest request) {
        Response response = Response.exception();
        response.addErrorMsgToResponse(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExceptionManager.DuplicateEntityException.class)
    public final ResponseEntity handleDuplicateEntityException(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExceptionManager.UnauthorizedEntityException.class)
    public final ResponseEntity handleUnauthorizedEntityException(Exception ex, WebRequest request) {
        Response response = Response.unauthorized();
        response.addErrorMsgToResponse(ex.getMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

}
