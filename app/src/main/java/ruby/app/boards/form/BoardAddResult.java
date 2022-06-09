package ruby.app.boards.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardAddResult {

    private boolean status;
    private String message;
    private Long id;
}
