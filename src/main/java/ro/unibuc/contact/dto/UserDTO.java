package ro.unibuc.contact.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

public class UserDTO {
        
        @NotBlank(message = "Username is mandatory")
        private String username;
        
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        private String email;
        
        @NotBlank(message = "Password is mandatory")
        private String password;
    
        public UserDTO() {
    
        }
    
        public UserDTO(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    
        public String getUsername() {
            return username;
        }
    
        public String getEmail() {
            return email;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    
}
