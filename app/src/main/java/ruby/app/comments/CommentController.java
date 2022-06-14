package ruby.app.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ruby.app.account.form.LoginAccount;
import ruby.app.boards.apiResult.ApiResult;
import ruby.app.comments.form.CommentAddForm;
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
    public ResponseEntity<ApiResult<Long>> addComment(
            @LoginAccount Account account, @RequestBody @Validated CommentAddForm commentAddForm, BindingResult bindingResult) {
        // 댓글을 등록한다. 갱신된 정보는 리다이렉트로 처리한다.
        String errorMessage = getApiResultResponseEntity(bindingResult);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new ApiResult<>(errorMessage, null));
        }

        commentService.addComment(commentAddForm.getContents(), commentAddForm.getBoardId(), account);

        return ResponseEntity.ok().body(new ApiResult<>(null, null));
    }

    /**
     * 댓글 삭제
     * @param account
     * @param commentId
     * @return
     */
    @DeleteMapping("{commentId}")
    public ResponseEntity<ApiResult<Long>> deleteComment(@LoginAccount Account account, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body(new ApiResult<>(null, null));
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
