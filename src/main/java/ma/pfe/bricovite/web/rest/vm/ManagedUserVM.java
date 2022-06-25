package ma.pfe.bricovite.web.rest.vm;

import javax.validation.constraints.Size;
import ma.pfe.bricovite.domain.User;
import ma.pfe.bricovite.service.dto.AdminUserDTO;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private String authorite;

    private String tel;

    private String cnie;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVM(String authorite) {
        this.authorite = authorite;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCnie() {
        return cnie;
    }

    public void setCnie(String cnie) {
        this.cnie = cnie;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorite() {
        return authorite;
    }

    public void setAuthorite(String authorite) {
        this.authorite = authorite;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
