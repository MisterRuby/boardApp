package ruby.app.account.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter @Setter
public class ProfileForm {

    @Size(max = 50, message = "자기소개는 최대 50자까지만 입력할 수 있습니다.")
    private String bio;             // 자기소개

    private String profileImage;    // 프로필 이미지
}
