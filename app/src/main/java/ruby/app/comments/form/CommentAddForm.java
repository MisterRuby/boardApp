package ruby.app.comments.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class CommentAddForm {

    @NotNull(message = "해당 게시글을 찾을 수 없습니다.")
    private Long boardId;
    private String contents;
}
