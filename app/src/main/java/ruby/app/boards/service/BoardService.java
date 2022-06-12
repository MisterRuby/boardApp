package ruby.app.boards.service;

import org.springframework.data.domain.Page;
import ruby.app.boards.form.SearchOption;
import ruby.app.domain.Account;
import ruby.app.domain.Board;

public interface BoardService {

    /**
     * 게시글 등록
     * @param title
     * @param contents
     * @param account
     * @return
     */
    Board addBoard(String title, String contents, Account account);


    /**
     * 게시글 상세 조회
     * @param boardId
     * @return
     */
    Board lookupBoard(Long boardId);

    /**
     * 게시글 목록 조회
     * @param pageNum
     * @param searchOption
     * @param searchWord
     * @return
     */
    Page<Board> lookupBoards(int pageNum, SearchOption searchOption, String searchWord);
}
