package ruby.app.comments.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.repository.BoardRepository;
import ruby.app.comments.repository.CommentRepository;
import ruby.app.comments.service.CommentService;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     * @param contents
     * @param boardId
     * @param account
     */
    @Override
    public void addComment(String contents, Long boardId, Account account) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        optionalBoard.ifPresentOrElse(
                board -> {
                    Comment comment = Comment.builder()
                            .contents(contents)
                            .board(board)
                            .account(account)
                            .createAt(LocalDateTime.now())
                            .build();
                    commentRepository.save(comment);
                },
                () -> {throw new IllegalStateException("해당 게시글을 찾을 수 없습니다.");}
        );
    }
}
