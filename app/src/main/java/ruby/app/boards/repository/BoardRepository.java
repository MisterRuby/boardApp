package ruby.app.boards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.app.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
