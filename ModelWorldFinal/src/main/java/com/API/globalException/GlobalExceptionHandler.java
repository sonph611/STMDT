package com.API.globalException;

import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.API.utils.ObjectRespone;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ObjectRespone> handleValidationExceptions(MethodArgumentNotValidException ex) {
		ObjectRespone object=new ObjectRespone(400, null,null);
		for (ObjectError a: ex.getBindingResult().getAllErrors()) {
				object.setMessage(a.getDefaultMessage());
				break;
		}
        return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
    }
	
	
	@ExceptionHandler(CustomeException.class)
    public ResponseEntity<ObjectRespone> handleValidationExceptionsa(CustomeException ex) {
		ObjectRespone object=new ObjectRespone(400,ex.getMessage(),null);
//		for (ObjectError a: ex.getBindingResult().getAllErrors()) {
//				object.setMessage(a.getDefaultMessage());
//				break;
//		}
        return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
    }
	
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ObjectRespone> handleValidationExceptions(Exception ex) {
//    	System.out.println("Hello woeld");
		ObjectRespone object=new ObjectRespone(400,"Yêu cầu xử lý thất bại, vui lòng thử lại sao",null);
//		for (ObjectError a: ex.getBindingResult().getAllErrors()) {
//				object.setMessage(a.getDefaultMessage());
//				break;
//		}
        return new ResponseEntity<>(object, HttpStatus.OK);
    }
	
	
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ObjectRespone> handleConstraintViolationException(ConstraintViolationException ex) {
//		ObjectRespone object=new ObjectRespone(400, "Dữ liêu",null);
//		System.out.println("Jjdlchdh");
////		for (ObjectError a: ex.getBindingResult().getAllErrors()) {
////				object.setMessage(a.getDefaultMessage());
////				break;
////		}
//        return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
//    }
	
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectRespone(400, "Dữ liệu truyền vào không đúng định dang", null));
    }
    
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleNumberFormatException(NumberFormatException ex) {
        return new ObjectRespone(400, "Không đúng định dạng yêu cầu", null);
    }
    
}	
