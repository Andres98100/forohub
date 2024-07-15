package andres.forohub.infra.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatchErrors {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> catchError404(){
        return ResponseEntity.notFound().build();
    }

    // manejar error 403 Forbidden
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> catchError403(SecurityException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> catchError400(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors().stream()
                .map(DtoErrors::new)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    public record DtoErrors(String field, String message) {
        public DtoErrors(FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
