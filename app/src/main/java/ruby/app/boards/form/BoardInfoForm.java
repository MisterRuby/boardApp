package ruby.app.boards.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class BoardInfoForm {

    private Long writerId;
    private String writerNickname;
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private LocalDate boardCreateAt;
    private Integer boardVisited;
    private Integer boardRecommend;

    public BoardInfoForm(Board board) {
        Account writer = board.getAccount();

        this.writerId = writer.getId();
        this.writerNickname = writer.getNickname();
        this.boardId = board.getId();
        this.boardTitle = board.getTitle();
        this.boardContent = board.getContents();
        this.boardCreateAt = board.getCreateAt().toLocalDate();
        this.boardVisited = board.getVisited();
        this.boardRecommend = board.getRecommend();
    }
}
