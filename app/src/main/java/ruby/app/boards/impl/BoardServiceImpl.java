package ruby.app.boards.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.BoardService;
import ruby.app.boards.repository.BoardRepository;
import ruby.app.domain.Account;
import ruby.app.domain.Board;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 등록
     * @param title
     * @param contents
     * @param account
     * @return
     */
    @Override
    public Board addBoard(String title, String contents, Account account) {
        Board newBoard = Board.builder()
                .title(title)
                .contents(contents)
                .createAt(LocalDateTime.now())
                .recommend(0)
                .visited(0)
                .account(account)
                .build();

        return boardRepository.save(newBoard);
    }
}
