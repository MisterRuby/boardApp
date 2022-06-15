package ruby.app.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.comments.form.CommentAddForm;
import ruby.app.comments.repository.CommentRepository;
import ruby.app.comments.service.CommentService;
import ruby.app.domain.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@ActiveProfiles("dev")
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 등록")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addCommentByNotExistsBoard() throws Exception {
        CommentAddForm commentAddForm = new CommentAddForm();
        commentAddForm.setBoardId(20000L);
        commentAddForm.setContents("댓글을 달아봅니다.");

        mockMvc.perform(post("/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentAddForm))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("/errors/404"));

    }

    @Test
    @DisplayName("게시글에 댓글 등록")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addComment() throws Exception {
        CommentAddForm commentAddForm = new CommentAddForm();
        commentAddForm.setBoardId(25L);
        commentAddForm.setContents("댓글을 달아봅니다.");

        mockMvc.perform(post("/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentAddForm))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/comments/51")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Comment> comment = commentRepository.findById(9L);
        assertThat(comment.isEmpty()).isTrue();
    }

}