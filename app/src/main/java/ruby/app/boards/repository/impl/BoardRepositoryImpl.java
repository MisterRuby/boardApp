package ruby.app.boards.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.app.boards.repository.BoardRepositoryCustom;
import ruby.app.domain.Board;

import static ruby.app.domain.QAccount.account;
import static ruby.app.domain.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 게시글
     * @param boardId
     * @return
     */
    @Override
    public Board findBoardAndWriter(Long boardId) {
        return queryFactory.selectFrom(board)
                .leftJoin(board.account, account).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne();
    }
}
