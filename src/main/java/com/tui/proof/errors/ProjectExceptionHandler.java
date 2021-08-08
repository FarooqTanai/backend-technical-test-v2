package com.tui.proof.errors;


import static com.tui.proof.utils.Util.PLACE_HOLDER;
import static org.springframework.http.ResponseEntity.status;

import com.tui.proof.model.Entity;
import com.tui.proof.model.response.ErrorDetails;
import com.tui.proof.model.response.ErrorResponse;
import com.tui.proof.utils.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProjectExceptionHandler {
	

	@ExceptionHandler(ClientsNotFoundException.class)
	public ResponseEntity<ErrorResponse> clientsNotFoundException() {
		ErrorDetails error = new ErrorDetails(Errors.CLIENTS_NOT_FOUND_CODE, Errors.CLIENTS_NOT_FOUND_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.CLIENT, error);
		return status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<ErrorResponse> clientNotFoundException(ClientNotFoundException e) {
		String message = Errors.CLIENT_NOT_FOUND_MESSAGE.replace(PLACE_HOLDER, e.getClientId());
		ErrorDetails error = new ErrorDetails(Errors.CLIENT_NOT_FOUND_CODE, message, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.CLIENT, error);
		return status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(PilotesNotAllowedException.class)
	public ResponseEntity<ErrorResponse> pilotesNotAllowedException(PilotesNotAllowedException e) {
		String message = Errors.PILOTES_NOT_ALLOWED_MESSAGE.replace(PLACE_HOLDER, String.valueOf(e.getAllowedPilotes()));
		ErrorDetails error = new ErrorDetails(Errors.PILOTES_NOT_ALLOWED_CODE, message, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.ORDER, error);
		return status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> orderNotFoundException(OrderNotFoundException e) {
		String message = Errors.ORDER_NOT_FOUND_MESSAGE.replace(PLACE_HOLDER, e.getOrderNumber());
		ErrorDetails error = new ErrorDetails(Errors.ORDER_NOT_FOUND_CODE, message, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.ORDER, error);
		return status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(OrderNotUpdatableException.class)
	public ResponseEntity<ErrorResponse> orderNotUpdatableException() {
		ErrorDetails error = new ErrorDetails(Errors.ORDER_NOT_UPDATABLE_CODE, Errors.ORDER_NOT_UPDATABLE_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.ORDER, error);
		return status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@ExceptionHandler(FieldMissingException.class)
	public ResponseEntity<ErrorResponse> fieldMissingException(FieldMissingException e) {
		String message = Errors.FIELD_MISSING_MESSAGE.replace(PLACE_HOLDER, e.getField());
		ErrorDetails error = new ErrorDetails(Errors.FIELD_MISSING_CODE, message, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(e.getEntity(), error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(EmailNotValidException.class)
	public ResponseEntity<ErrorResponse> emailNotValidException() {
		ErrorDetails error = new ErrorDetails(Errors.EMAIL_NOT_VALID_CODE, Errors.EMAIL_NOT_VALID_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.CLIENT, error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(TelephoneNotValidException.class)
	public ResponseEntity<ErrorResponse> telephoneNotValidException() {
		ErrorDetails error = new ErrorDetails(Errors.TELEPHONE_NOT_VALID_CODE, Errors.TELEPHONE_NOT_VALID_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.CLIENT, error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(SexNotValidException.class)
	public ResponseEntity<ErrorResponse> sexNotValidException() {
		ErrorDetails error = new ErrorDetails(Errors.SEX_NOT_VALID_CODE, Errors.SEX_NOT_VALID_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.CLIENT, error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(OrderTotalisNotValidException.class)
	public ResponseEntity<ErrorResponse> orderTotalisNotValidException() {
		ErrorDetails error = new ErrorDetails(Errors.ORDER_TOTAL_NOT_VALID_CODE, Errors.ORDER_TOTAL_NOT_VALID_MESSAGE, Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.ORDER, error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		ErrorDetails error = new ErrorDetails(Errors.GENERIC_ERROR_CODE, e.getMessage(), Util.getCurrentDate());
		ErrorResponse response = new ErrorResponse(Entity.GENERIC, error);
		return status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	

}
