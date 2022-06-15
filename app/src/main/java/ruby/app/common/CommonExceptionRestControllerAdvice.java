package ruby.app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ruby.app.boards.apiResult.ApiResult;

@Slf4j
@RestControllerAdvice
public class CommonExceptionRestControllerAdvice {

    /**
     * AccessDeniedException 발생시 403 반환
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error("[accessDeniedExceptionHandler] ex", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResult<>(e.getMessage(), null));
    }


    /**
     * 다른 예외들이 처리하지 못한 기타 예외 처리
     */
    @ExceptionHandler
    public ResponseEntity exceptionHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResult<>(e.getMessage(), null));
    }
}
