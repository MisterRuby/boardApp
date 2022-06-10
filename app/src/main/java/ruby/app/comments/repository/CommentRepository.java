package ruby.app.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.app.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
