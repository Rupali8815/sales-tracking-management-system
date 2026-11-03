package com.company.salestracker.dto.request;

import com.company.salestracker.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

      private String password;

    private String phone;

    private UserStatus status;

     private Set<Integer> roleIds;
}
