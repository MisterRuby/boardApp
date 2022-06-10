package ruby.app.boards.service;

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
     * 게시글 상세 조회
     * @param boardId
     * @return
     */
    Board inquireBoard(Long boardId);
}
