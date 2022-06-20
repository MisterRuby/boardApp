package ruby.app.boards;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ruby.app.account.form.LoginAccount;
import ruby.app.boards.apiResult.ApiResult;
import ruby.app.boards.form.*;
import ruby.app.boards.service.BoardService;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.domain.Comment;
import ruby.app.util.paging.Paging;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String boards(@LoginAccount Account account,
                         BoardSearchForm boardSearchForm,
                         Model model) {

        Page<Board> boards =
                boardService.lookupBoards(boardSearchForm.getPageNum(), boardSearchForm.getSearchOption(), boardSearchForm.getSearchWord());
        List<BoardForm> boardFormList = boards.stream().map(BoardForm::new).collect(Collectors.toList());
        Paging paging = new Paging().setPagingNumbers(boards.getPageable(), boards.getTotalPages());

        if (account != null) model.addAttribute(account);
        model.addAttribute("boards", boardFormList);
        model.addAttribute("paging", paging);
        model.addAttribute("optionList", SearchOption.values());

        return "boards/boards";
    }

    /**
     * 게시글 상세 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}")
    public String board(@PathVariable Long boardId, @LoginAccount Account account, Model model) {
        Board board = boardService.lookupBoardAndComments(boardId);
        if (board == null) return "redirect:/boards";

        List<BoardInfoCommentForm> boardInfoCommentForms = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            boardInfoCommentForms.add(new BoardInfoCommentForm(comment));
        }

        if (account != null) model.addAttribute(account);
        model.addAttribute("board", new BoardInfoForm(board));
        model.addAttribute("comments", boardInfoCommentForms);

        return "boards/board";
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
    public ResponseEntity<ApiResult<Long>> addBoard(@LoginAccount Account account, @RequestBody @Validated BoardAddForm boardAddForm, BindingResult bindingResult) {
        String errorMessage = getApiResultResponseEntity(bindingResult);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new ApiResult<>(errorMessage, null));
        }

        Board addBoard = boardService.addBoard(boardAddForm.getTitle(), boardAddForm.getContents(), account);
        return ResponseEntity.ok().body(new ApiResult<>("글이 등록되었습니다.", addBoard.getId()));
    }


    /**
     * 게시글 수정 페이지 이동
     * @return
     */
    @GetMapping("/{boardId}/edit")
    public String editForm(@LoginAccount Account account, @PathVariable Long boardId, Model model) {
        if (account != null) model.addAttribute(account);
        Board board = boardService.lookupBoard(boardId, account);

        model.addAttribute(modelMapper.map(board, BoardEditForm.class));
        return "boards/editForm";
    }

    /**
     * 게시글 수정
     * @return
     */
    @PatchMapping("/{boardId}/edit")
    @ResponseBody
    public ResponseEntity<ApiResult<Long>> editBoard(@LoginAccount Account account, @PathVariable Long boardId,
                            @RequestBody @Validated BoardEditForm boardEditForm, BindingResult bindingResult) {

        String errorMessage = getApiResultResponseEntity(bindingResult);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new ApiResult<>(errorMessage, null));
        }

        Optional<Board> board = boardService.updateBoard(account.getId(), boardId, boardEditForm.getTitle(), boardEditForm.getContents());
        if (board.isPresent()) {
            return ResponseEntity.ok().body(new ApiResult<>("글이 수정되었습니다.", boardId));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResult<>("해당 글을 찾을 수 없습니다.", null));
    }

    /**
     * 게시글 삭제
     * @param account
     * @param boardId
     * @return
     */
    @DeleteMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<ApiResult<Object>> deleteBoard(@LoginAccount @ModelAttribute Account account, @PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().body(new ApiResult<>("삭제되었습니다.", null));
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
