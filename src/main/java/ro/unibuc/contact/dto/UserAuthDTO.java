package ro.unibuc.contact.dto;

import javax.validation.constraints.NotBlank;

public class UserAuthDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    public UserAuthDTO() {
    }

    public UserAuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}