package ruby.app.boards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.form.BoardAddForm;
import ruby.app.boards.form.BoardEditForm;
import ruby.app.boards.repository.BoardRepository;
import ruby.app.boards.service.BoardService;
import ruby.app.domain.Board;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/*
    게시글 목록 페이지 이동 - 보류
    게시글 상세 페이지 이동
    게시글 작성 페이지 이동
    게시글 등록
    게시글 수정 페이지 이동 - 보류
*/
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ActiveProfiles("dev")
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @DisplayName("없는 페이지 번호로 게시글 목록 조회")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void boardsNotExistsPageNum() throws Exception {
        mockMvc.perform(get("/boards")
                        .param("pageNum", "999")
                        .param("searchOption", "TITLE")
                        .param("searchWord", "테스트")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/boards/boards"))
                .andExpect(model().attribute("boards", new ArrayList<>()));
    }

    @Test
    @DisplayName("없는 옵션으로 게시글 목록 조회")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void boardsNotExistsSearchOption() throws Exception {
        mockMvc.perform(get("/boards")
                        .param("pageNum", "999")
                        .param("searchOption", "EMPTY")
                        .param("searchWord", "ㄷㅁ넖몬어ㅠ란유라")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("게시글 목록 페이지 이동 및 조회")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void boards() throws Exception {
        mockMvc.perform(get("/boards")
                        .param("pageNum", "0")
                        .param("searchOption", "TITLE")
                        .param("searchWord", "테스트")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/boards/boards"))
                .andExpect(model().attributeExists("boards"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("optionList"));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 상세 조회")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void notExistsBoard() throws Exception {
        mockMvc.perform(get("/boards/20000"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("/errors/404"));
    }

    @Test
    @DisplayName("게시글 상세 페이지 이동")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void board() throws Exception {
        mockMvc.perform(get("/boards/25"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/boards/board"))
                .andExpect(model().attributeExists("board"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    @DisplayName("게시글 작성 페이지 이동")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addBoardForm() throws Exception {
        mockMvc.perform(get("/boards/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/boards/addForm"));
    }

    @Test
    @DisplayName("게시글 등록 시 제목 오류")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addBoardWrongTitle() throws Exception {
        BoardAddForm boardAddForm = new BoardAddForm();
        boardAddForm.setTitle("1");
        boardAddForm.setContents("글을 등록해요.");

        mockMvc.perform(post("/boards/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardAddForm))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("게시글 등록")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addBoard() throws Exception {
        BoardAddForm boardAddForm = new BoardAddForm();
        boardAddForm.setTitle("글 등록 테스트");
        boardAddForm.setContents("글을 등록해요.");

        mockMvc.perform(post("/boards/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardAddForm))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 페이지 요청")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void editNotBoard() throws Exception {
        mockMvc.perform(get("/boards/999/edit"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("/errors/404"));
    }

    @Test
    @DisplayName("게시글 수정 페이지 이동")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void editBoardForm() throws Exception {
        mockMvc.perform(get("/boards/25/edit"))
                .andDo(print())
                .andExpect(view().name("/boards/editForm"))
                .andExpect(model().attributeExists("boardEditForm"));
    }

    @Test
    @DisplayName("제목이 두 글자 미만 시 게시글 수정 실패")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateBoardErrorTitle() throws Exception {
        Board board = boardService.lookupBoard(25L);
        board.setTitle("한");

        mockMvc.perform(patch("/boards/25/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelMapper.map(board, BoardEditForm.class)))
                .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateBoardExistsBoard() throws Exception {
        Board board = new Board();
        board.setId(999L);
        board.setTitle("수정됨");

        mockMvc.perform(patch("/boards/999/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(board, BoardEditForm.class)))
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("게시글 수정")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateBoard() throws Exception {
        Board board = boardService.lookupBoard(25L);
        board.setTitle("수정됨");

        mockMvc.perform(patch("/boards/25/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelMapper.map(board, BoardEditForm.class)))
                        .with(csrf()))
                .andExpect(status().isOk());

        Board updatedBoard = boardService.lookupBoard(25L);
        assertThat(updatedBoard.getTitle()).isEqualTo("수정됨");
    }

    @Test
    @DisplayName("게시글 삭제")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteBoard() throws Exception {
        mockMvc.perform(delete("/boards/25/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        Optional<Board> board = boardRepository.findById(25L);
        assertThat(board.isEmpty()).isTrue();
    }
}
















