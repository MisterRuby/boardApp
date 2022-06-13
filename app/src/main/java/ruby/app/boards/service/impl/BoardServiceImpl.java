package ruby.app.boards.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.form.SearchOption;
import ruby.app.boards.repository.BoardRepository;
import ruby.app.boards.service.BoardService;
import ruby.app.comments.repository.CommentRepository;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final int PAGE_PER_MAX_COUNT = 10;

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
    public Board lookupBoard(Long boardId) {
        Board board = boardRepository.findBoardAndWriter(boardId);
        if (board != null) {
            board.addVisited();
            List<Comment> comments = commentRepository.findCommentsAndWriter(board);
            board.setComments(comments);
        }
        return board;
    }

    /**
     * 게시글 목록 조회
     * @param pageNum
     * @param searchOption
     * @param searchWord
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Board> lookupBoards(int pageNum, SearchOption searchOption, String searchWord) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_PER_MAX_COUNT, Sort.by(searchOption.name()).descending());
        return boardRepository.findBoards(searchOption, searchWord, pageable);
    }

    /**
     * 게시글 수정
     * @param boardId
     * @param title
     * @param contents
     * @return
     */
    @Override
    public Optional<Board> updateBoard(Long boardId, String title, String contents) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        optionalBoard.ifPresent(board -> {
            board.setTitle(title);
            board.setContents(contents);
            boardRepository.save(board);
        });

        return optionalBoard;
    }

    /**
     * 게시글 삭제
     * @param boardId
     */
    @Override
    public void deleteBoard(Long boardId) {
        // 댓글 삭제 - 벌크로 삭제해야함
        commentRepository.deleteCommentsByBoard(boardId);
        // 게시글 삭제
        boardRepository.deleteById(boardId);
    }
}
