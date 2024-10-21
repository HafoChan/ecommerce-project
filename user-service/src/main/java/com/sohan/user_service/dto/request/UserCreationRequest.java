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

    @NotBlank(message = "USERNAME_NOT_BLANK")
    @Size(min = 3, message = "USERNAME_SIZE")
    String username;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min = 6, message = "PASSWORD_SIZE")
    String password;

    @NotBlank(message = "FULL_NAME_NOT_BLANK")
    @Size(min = 5, message = "FULL_NAME_SIZE")
    String fullName;

    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_NUMBER_PATTERN")
    String phone;
}
