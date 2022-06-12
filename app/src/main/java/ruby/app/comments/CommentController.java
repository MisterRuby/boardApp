package ruby.app.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ruby.app.account.form.LoginAccount;
import ruby.app.comments.form.CommentAddForm;
import ruby.app.comments.form.CommentAddResult;
import ruby.app.comments.service.CommentService;
import ruby.app.domain.Account;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     * @param account
     * @param commentAddForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<CommentAddResult> addComment(
            @LoginAccount Account account, @RequestBody @Validated CommentAddForm commentAddForm, BindingResult bindingResult) {
        // 댓글을 등록한다. 갱신된 정보는 리다이렉트로 처리한다.
        String errorMessage = getApiResultResponseEntity(bindingResult);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new CommentAddResult(false, null));
        }

        commentService.addComment(commentAddForm.getContents(), commentAddForm.getBoardId(), account);

        return ResponseEntity.ok().body(new CommentAddResult(true, null));
    }

    /**
     * 글 등록시 필드 에러 확인 및 메시지
     * @param bindingResult
     * @return
     */
    private String getApiResultResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                return fieldError.getDefaultMessage();
            }
        }
        return null;
    }
}
