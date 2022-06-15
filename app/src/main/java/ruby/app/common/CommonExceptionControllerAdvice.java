package ruby.app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CommonExceptionControllerAdvice {

    /**
     * IllegalArgumentException 발생시 404 페이지 반환
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalExceptionHandler(IllegalArgumentException e) {
        log.error("[illegalExceptionHandler] ex", e);
        return "/errors/404";
    }

    /**
     * AccessDeniedException 발생시 403 페이지 반환
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error("[accessDeniedExceptionHandler] ex", e);
        return "/errors/403";
    }


    /**
     * 다른 예외들이 처리하지 못한 기타 예외 처리
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public String exceptionHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return "/errors/500";
    }
}
