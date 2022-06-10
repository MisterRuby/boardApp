package ruby.app.boards;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ruby.app.account.form.LoginAccount;
import ruby.app.boards.form.*;
import ruby.app.boards.service.BoardService;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;            // 게시판 service
    private final ModelMapper modelMapper;              // ModelMapper


    /**
     * 게시글 목록 페이지 이동
     * @return
     */
    @GetMapping
    public String boards(@LoginAccount Account account, Model model) {
        if (account != null) model.addAttribute(account);
        return "/boards/boards";
    }

    /**
     * 게시글 상세 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}")
    public String board(@PathVariable Long boardId, @LoginAccount Account account, Model model) {
        Board board = boardService.inquireBoard(boardId);
        List<BoardInfoCommentForm> boardInfoCommentForms = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            boardInfoCommentForms.add(new BoardInfoCommentForm(comment));
        }

        if (account != null) model.addAttribute(account);
        model.addAttribute("board", new BoardInfoForm(board));
        model.addAttribute("comments", boardInfoCommentForms);

        return "/boards/board";
    }

    /**
     * 게시글 작성 페이지 이동
     * @return
     */
    @GetMapping("/add")
    public String addForm(@ModelAttribute @LoginAccount Account account) {
        return "/boards/addForm";
    }

    /**
     * 게시글 등록
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity addBoard(@LoginAccount Account account, @RequestBody @Validated BoardAddForm boardAddForm, BindingResult bindingResult) {
        String errorMessage = getApiResultResponseEntity(bindingResult);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new BoardAddResult(false, errorMessage, null));
        }

        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BoardAddResult(false,"로그인이 해제되었습니다. 다시 로그인해주세요.", null));
        }

        Board addBoard = boardService.addBoard(boardAddForm.getTitle(), boardAddForm.getContents(), account);
        return ResponseEntity.ok().body(new BoardAddResult(true,"글이 등록되었습니다.", addBoard.getId()));
    }


    /**
     * 게시글 수정 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId) {
        return "/boards/editForm";
    }


    /**
     * 글 등록시 필드 에러 확인 및 메시지
     * @param bindingResult
     * @return
     */
    private String getApiResultResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                return fieldError.getDefaultMessage();
            }
        }
        return null;
    }
}
