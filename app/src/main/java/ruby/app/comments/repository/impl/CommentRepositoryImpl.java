package ruby.app.comments.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.app.comments.repository.CommentRepositoryCustom;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;
import ruby.app.domain.QBoard;
import ruby.app.domain.QComment;

import java.util.List;

import static ruby.app.domain.QAccount.account;
import static ruby.app.domain.QBoard.*;
import static ruby.app.domain.QComment.*;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 게시글에 엮인 댓글 조회
     * @param belongBoard
     * @return
     */
    @Override
    public List<Comment> findCommentsAndWriter(Board belongBoard) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.account, account).fetchJoin()
                .leftJoin(comment.board, board).fetchJoin()
                .where(comment.board.eq(belongBoard))
                .fetch();
    }

}
