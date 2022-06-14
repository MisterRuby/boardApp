package ruby.app.util.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter @Setter
public class Paging {
    public static final int PAGE_PER_MAX_COUNT = 10;
    private int start;
    private int end;
    private int now;
    private int totalEnd;

    public Paging setPagingNumbers(Pageable pageable, int totalPage) {
        this.now = pageable.getPageNumber();
        this.start = this.now / 10 * 10;
        this.totalEnd = totalPage - 1;
        this.end = Math.min(this.start + 9, totalEnd);

        return this;
    }
}
