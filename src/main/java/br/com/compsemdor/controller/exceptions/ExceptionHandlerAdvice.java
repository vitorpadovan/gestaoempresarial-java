package br.com.compsemdor.controller.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.badRequest().body(new ErrorResponse(ex));
	}

	@ExceptionHandler(AplicationException.class)
	protected ResponseEntity<ErrorResponse> handleAplicationException(AplicationException ex) {
		return ResponseEntity.badRequest().body(new ErrorResponse(ex));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		var response = new ErrorResponse();
		for (var t : ex.getAllErrors()) {
			response = response.addErrors(ex.getAllErrors().indexOf(t) + ". Erro de validação de campo",
					t.getDefaultMessage());
		}
		return ResponseEntity.ok(response);
	}
}
