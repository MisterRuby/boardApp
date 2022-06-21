package ruby.app.boards.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class BoardAddForm {

    @Length(min = 2, message = "글 제목은 최소 2글자 이상이어야 합니다.")
    private String title;
    @Length(max = 4000, message = "글 본문은 최대 4000자를 이하이어야 합니다.")
    private String contents;
}
