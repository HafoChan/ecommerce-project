package com.sohan.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, message = "Username length should be more than 3 characters")
    String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password length should be at least 6 characters")
    String password;

    @NotBlank(message = "Full name is mandatory")
    @Size(min = 5, message = "Full name length should be more than 5 characters")
    String fullName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    String phone;
}
