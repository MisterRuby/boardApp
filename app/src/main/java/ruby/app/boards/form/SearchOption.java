package ruby.app.boards.form;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum SearchOption {

    ALL("전체"),
    TITLE("제목"),
    CONTENTS("내용"),
    NICKNAME("닉네임");

    private String text;
    SearchOption(String text) {
        this.text = text;
    }
}
