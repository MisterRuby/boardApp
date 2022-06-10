package ruby.app.boards.repository;

import ruby.app.domain.Board;

import java.util.Optional;

public interface BoardRepositoryCustom {
    Board findBoardAndWriter(Long boardId);
}
