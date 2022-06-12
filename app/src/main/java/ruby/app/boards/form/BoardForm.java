package ruby.app.boards.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.app.domain.Board;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class BoardForm {

    private Long boardId;
    private String title;
    private Integer replyCount;
    private Integer recommend;
    private Integer visited;
    private String nickname;
    private LocalDate createAt;

    public BoardForm (Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.replyCount = board.getReplyCount();
        this.recommend = board.getRecommend();
        this.visited = board.getVisited();
        this.nickname = board.getAccount().getNickname();
        this.createAt = board.getCreateAt().toLocalDate();
    }
}
