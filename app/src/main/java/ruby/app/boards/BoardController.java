package ruby.app.boards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class BoardController {

    /**
     * 게시글 목록 페이지 이동
     * @return
     */
    @GetMapping
    public String boards() {
        return "/boards/boards";
    }

    /**
     * 게시글 상세 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}")
    public String board(@PathVariable Long boardId) {
        return "/boards/board";
    }

    /**
     * 게시글 작성 페이지 이동
     * @return
     */
    @GetMapping("/add")
    public String addForm() {
        return "/boards/addForm";
    }

    /**
     * 게시글 수정 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId) {
        return "/boards/editForm";
    }
}
