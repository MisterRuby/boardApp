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
}
