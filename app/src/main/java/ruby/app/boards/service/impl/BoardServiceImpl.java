package ruby.app.boards.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.repository.BoardRepository;
import ruby.app.boards.service.BoardService;
import ruby.app.comments.repository.CommentRepository;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

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

    /**
     * 게시글 상세 조회
     * @param boardId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Board inquireBoard(Long boardId) {
        Board board = boardRepository.findBoardAndWriter(boardId);
        List<Comment> comments = commentRepository.findCommentsAndWriter(board);
        board.setComments(comments);

        return board;
    }
}
