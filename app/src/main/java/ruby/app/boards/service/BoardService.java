package ruby.app.boards.service;

import org.springframework.data.domain.Page;
import ruby.app.boards.form.SearchOption;
import ruby.app.domain.Account;
import ruby.app.domain.Board;

import java.util.Optional;

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
     * 게시글 상세 조회 - 단순 조회 및 댓글 포함 조회
     * @param boardId
     * @return
     */
    Board lookupBoardAndComments(Long boardId);

    /**
     * 게시글 상세 조회 - 수정을 위한 게시글 조회
     * @param boardId
     * @param account
     * @return
     */
    Board lookupBoard(Long boardId, Account account);

    /**
     * 게시글 목록 조회
     * @param pageNum
     * @param searchOption
     * @param searchWord
     * @return
     */
    Page<Board> lookupBoards(int pageNum, SearchOption searchOption, String searchWord);

    /**
     * 게시글 수정
     * @param accountId
     * @param boardId
     * @param title
     * @param contents
     * @return
     */
    Optional<Board> updateBoard(Long accountId, Long boardId, String title, String contents);

    /**
     * 게시글 삭제
     * @param boardId
     */
    void deleteBoard(Long boardId);


}
