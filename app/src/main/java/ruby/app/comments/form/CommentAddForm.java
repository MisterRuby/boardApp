package ruby.app.comments.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class CommentAddForm {

    @NotNull
    private Long boardId;
    private String contents;
}
