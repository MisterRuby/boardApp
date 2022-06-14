package ruby.app.comments.service;

import ruby.app.domain.Account;

public interface CommentService {

    /**
     * 댓글 등록
     * @param contents
     * @param boardId
     * @param account
     */
    void addComment(String contents, Long boardId, Account account);

    /**
     * 댓글 삭제
     * @param commentId
     */
    void deleteComment(Long commentId);
}
