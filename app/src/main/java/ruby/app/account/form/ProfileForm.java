package ruby.app.account.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ruby.app.domain.Account;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class ProfileForm {

    @Size(max = 50)
    private String bio;             // 자기소개

    private String profileImage;    // 프로필 이미지

    public ProfileForm(Account account) {
        this.bio = account.getBio();
        this.profileImage = account.getProfileImage();
    }
}
