package ruby.app.comments.repository;

import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    /**
     * 게시글에 엮인 댓글 조회
     * @param belongBoard
     * @return
     */
    List<Comment> findCommentsAndWriter(Board belongBoard);
}
