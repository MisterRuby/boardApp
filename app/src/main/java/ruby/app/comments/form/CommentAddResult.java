package ruby.app.comments.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentAddResult {
    private boolean status;
    private String message;
}
