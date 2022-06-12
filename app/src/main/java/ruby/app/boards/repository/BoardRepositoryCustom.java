package ruby.app.boards.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ruby.app.boards.form.SearchOption;
import ruby.app.domain.Board;

import java.util.Optional;

public interface BoardRepositoryCustom {

    /**
     * 게시글 및 작성자 정보 조회
     * @param boardId
     * @return
     */
    Board findBoardAndWriter(Long boardId);

    /**
     * 게시글 목록 조회
     * @param searchOption
     * @param searchWord
     * @param pageable
     * @return
     */
    Page<Board> findBoards(SearchOption searchOption, String searchWord, Pageable pageable);
}
