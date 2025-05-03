package com.ead.authuser.dtos;

import com.ead.authuser.validation.UsernameContraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    public interface UserView {
        public static interface RegistrationPost {
        }

        public static interface UserPut {
        }

        public static interface PasswordPut {
        }

        public static interface ImagePut {
        }
    }


    private UUID userId;
    @Size(min = 4, max = 50)
    @NotBlank(groups = UserView.RegistrationPost.class)
    @UsernameContraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;
    @JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email
    private String email;
    @Size(min = 8, max = 20)
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;
    @JsonView(UserView.PasswordPut.class)
    @NotBlank(groups = UserView.PasswordPut.class)
    private String oldPassword;
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String fullname;
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;
    @CPF
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;
    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView({UserView.ImagePut.class})
    private String imageUrl;

}
