package ro.unibuc.contact.dto;

public class UpdateUserPasswordRequest {
    private String password;

    UpdateUserPasswordRequest() {
        
    }
    public UpdateUserPasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
