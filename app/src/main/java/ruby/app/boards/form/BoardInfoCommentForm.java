package ruby.app.boards.form;

import lombok.Getter;
import lombok.Setter;
import ruby.app.domain.Account;
import ruby.app.domain.Comment;

import java.time.LocalDate;

@Getter @Setter
public class BoardInfoCommentForm {

    private Long writerId;
    private String writerNickname;
    private Long commentId;
    private String commentContents;
    private LocalDate commentCreateAt;

    public BoardInfoCommentForm (Comment comment) {
        Account commentWriter = comment.getAccount();

        this.writerId = commentWriter.getId();
        this.writerNickname = commentWriter.getNickname();
        this.commentId = comment.getId();
        this.commentContents = comment.getContents();
        this.commentCreateAt = comment.getCreateAt().toLocalDate();
    }
}
