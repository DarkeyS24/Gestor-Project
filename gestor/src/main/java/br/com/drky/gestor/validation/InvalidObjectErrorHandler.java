package br.com.drky.gestor.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.drky.gestor.exception.ClientNotFoundException;
import br.com.drky.gestor.exception.InvalidClientDocumentException;
import br.com.drky.gestor.exception.InvalidObjectOnRequestException;
import br.com.drky.gestor.exception.RegisteredClientException;

@RestControllerAdvice
public class InvalidObjectErrorHandler {

	@Autowired
	private MessageSource ms;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErrorObject> handle(MethodArgumentNotValidException exception) {
		List<ErrorObject> dto = new ArrayList<>();

		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(fe -> {
			String message = ms.getMessage(fe, LocaleContextHolder.getLocale());
			ErrorObject error = new ErrorObject(fe.getField(), message);
			dto.add(error);
		});

		return dto;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ClientNotFoundException.class)
	public ErrorMessage handle2(ClientNotFoundException ex) {

		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RegisteredClientException.class)
	public ErrorMessage handle3(RegisteredClientException ex) {

		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidObjectOnRequestException.class)
	public ErrorMessage handle4(InvalidObjectOnRequestException ex) {

		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidClientDocumentException.class)
	public ErrorMessage handle5(InvalidClientDocumentException ex) {

		return new ErrorMessage(ex.getMessage());
	}
}