package ruby.app.boards.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchForm {
    private int pageNum = 0;
    private SearchOption searchOption = SearchOption.ALL;
    private String searchWord = "";
}
