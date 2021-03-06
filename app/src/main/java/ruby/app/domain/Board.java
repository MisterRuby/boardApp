package ruby.app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * 게시글 엔티티
 *  - 게시글 : 댓글 = 1 : N
 *  - 계정 : 게시글 = 1 : N
 */
@Entity
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Board {

    /** 컬럼 */
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "board_seq")
    @Column(name = "board_id")
    private Long id;                        // PK. 게시글 번호
    private String title;                   // 게시글 제목
    @Column(length = 4000)
    private String contents;                // 게시글 본문
    private LocalDateTime createAt;         // 작성일
    private int visited;                    // 방문자 수
    private int recommend;                  // 추천 수
    private int replyCount;                 // 댓글 수

    /** 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    /** 비즈니스 메서드 */

    public void addVisited () {
        this.visited++;
    }

    /**
     * 댓글 수 증가
     */
    public void addReplyCount() {
        this.replyCount++;
    }
}
