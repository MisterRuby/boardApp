package ruby.app.boards;

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
}
