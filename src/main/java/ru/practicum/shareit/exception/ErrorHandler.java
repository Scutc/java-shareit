package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Ошибка валидации полей объекта: {} {}. Путь запроса {}", e.getFieldError().getField(),
                e.getFieldError().getDefaultMessage(), request.getServletPath());
        return new ResponseEntity<>("Ошибка валидации полей объекта: " + e.getFieldError().getField()
                + " " + e.getFieldError().getDefaultMessage() + ". Путь запроса "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<String> handleDuplicateDataException(DuplicateDataException e, HttpServletRequest request) {
        log.warn("Дублирующиеся данные {} по пути запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<String> handleSecurityException(CustomSecurityException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.FORBIDDEN);
    }
}
