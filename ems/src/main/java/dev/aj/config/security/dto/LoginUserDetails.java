package dev.aj.config.security.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserDetails {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private List<String> roles = new ArrayList<>();
}
