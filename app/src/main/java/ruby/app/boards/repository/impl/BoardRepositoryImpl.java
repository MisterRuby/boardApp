package ruby.app.boards.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ruby.app.boards.form.SearchOption;
import ruby.app.boards.repository.BoardRepositoryCustom;
import ruby.app.domain.Board;
import ruby.app.domain.QComment;

import java.util.List;

import static ruby.app.domain.QAccount.account;
import static ruby.app.domain.QBoard.board;
import static ruby.app.domain.QComment.*;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 게시글 및 작성자 정보 조회
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

    /**
     * 게시글 목록 조회
     * @param searchOption
     * @param searchWord
     * @param pageable
     * @return
     */
    @Override
    public Page<Board> findBoards(SearchOption searchOption, String searchWord, Pageable pageable) {
        // 페이징을 위한 전체 목록 수
        Long totalCnt = queryFactory.select(board.count())
                .from(board)
                .leftJoin(board.account, account)
                .where(boardSearchCondition(searchOption, searchWord))
                .fetchOne();

        // 페이지에 해당하는 목록 조회
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.account, account).fetchJoin()
                .where(boardSearchCondition(searchOption, searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetch();

        return new PageImpl<>(boards, pageable, totalCnt);
    }


    /**
     * 게시글 검색조건 설정
     * @param searchOption
     * @param searchWord
     * @return
     */
    private BooleanExpression boardSearchCondition(SearchOption searchOption, String searchWord) {
        if (searchOption.equals(SearchOption.ALL)) {
            return board.title.contains(searchWord)
                    .or(board.contents.contains(searchWord))
                    .or(board.account.nickname.contains(searchWord));
        }
        if (searchOption.equals(SearchOption.TITLE)) {
            return board.title.contains(searchWord);
        }
        if (searchOption.equals(SearchOption.CONTENTS)) {
            return board.contents.contains(searchWord);
        }
        if (searchOption.equals(SearchOption.NICKNAME)) {
            return board.account.nickname.contains(searchWord);
        }


        return null;
    }
}
