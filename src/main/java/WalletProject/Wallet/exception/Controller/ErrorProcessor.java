package WalletProject.Wallet.exception.Controller;

import static org.springframework.http.HttpStatus.*;

import WalletProject.Wallet.exception.Exception.DataException;
import WalletProject.Wallet.exception.Exception.WalletNotFoundException;
import WalletProject.Wallet.exception.model.WrongAnswer;
import java.time.LocalDateTime;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorProcessor {

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public WrongAnswer handleNotFoundException(final WalletNotFoundException exception) {
        return new WrongAnswer(
                LocalDateTime.now(),
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataException.class)
    @ResponseStatus(BAD_REQUEST)
    public WrongAnswer handleDataException(final DataException exception) {
        return new WrongAnswer(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public WrongAnswer handleValidationException(final MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Invalid request");

        return new WrongAnswer(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                message
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public WrongAnswer handleHttpMessageNotReadableException() {
        return new WrongAnswer(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                "Invalid JSON format"
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(BAD_REQUEST)
    public WrongAnswer handleRuntimeException(final RuntimeException exception) {
        return new WrongAnswer(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                "Internal server error: " + exception.getMessage()
        );
    }
}
