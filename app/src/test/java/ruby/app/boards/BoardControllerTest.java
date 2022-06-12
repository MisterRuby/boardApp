package ruby.app.boards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.boards.form.BoardAddForm;
import ruby.app.boards.service.BoardService;

import javax.persistence.EntityManager;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    BoardService boardService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * 게시글 목록 페이지 이동 - TODO - 목록 조회까지 구현 후 테스트
     * @return
     */
    @Test
    @DisplayName("게시글 목록 페이지 이동 및 조회")
    void boards() {

    }

    @Test
    @DisplayName("존재하지 않는 게시글 상세 조회")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void notExistsBoard() throws Exception {
        mockMvc.perform(get("/boards/20000"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/boards"));
    }

    @Test
    @DisplayName("게시글 상세 페이지 이동")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void board() throws Exception {
        mockMvc.perform(get("/boards/15"))
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

    /**
     * 게시글 수정 페이지 이동 - TODO - 해당 게시글 조회 기능 추가 후 테스트
     * @return
     */
    @Test
    @DisplayName("게시글 수정 페이지 이동")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void editBoardForm() throws Exception {

    }
}