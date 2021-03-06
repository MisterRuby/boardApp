package ruby.app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

/**
 * 댓글 엔티티
 *  - 게시글 : 댓글 = 1 : N
 *  - 계정 : 댓글 = 1 : N
 */
@Entity
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Comment {

    /** 컬럼 */
    @Id
    @GeneratedValue
    private Long id;                    // PK
    private String contents;            // 댓글 내용
    private LocalDateTime createAt;     // 작성일

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public void setBoard(Board board) {
        if (this.board != null) {
            this.board.getComments().remove(this);
        }

        this.board = board;
        if (!board.getComments().contains(this)) {
            board.getComments().add(this);
        }
    }
}
