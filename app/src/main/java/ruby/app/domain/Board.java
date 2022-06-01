package ruby.app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
@Getter
public class Board {

    /** 컬럼 */
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "board_seq")
    @Column(name = "board_id")
    private Long id;                        // PK. 게시글 번호
    private String title;                   // 게시글 제목
    private String contents;                // 게시글 본문
    private LocalDateTime createAt;         // 작성일
    private Integer visited;                // 방문자 수
    private Integer recommend;              // 추천 수

    /** 연관관계 */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
