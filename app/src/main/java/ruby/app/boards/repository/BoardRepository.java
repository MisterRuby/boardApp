package ruby.app.boards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.app.domain.Account;
import ruby.app.domain.Board;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom{

    /**
     * 게시글 수정을 위한 게시글 조회
     * @param id
     * @param accountId
     * @return
     */
    Optional<Board> findByIdAndAccount_Id (Long id, Long accountId);
}
