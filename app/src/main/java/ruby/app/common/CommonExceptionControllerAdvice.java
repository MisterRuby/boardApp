package ruby.app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalExceptionHandler(IllegalArgumentException e) {
        log.error("[illegalExHandler] ex", e);
        return "/errors/404";
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
