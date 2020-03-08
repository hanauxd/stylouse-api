package lk.apiit.eea.stylouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String userId;
    private final String tokenValidation;
    private final String jwt;
    private final String userRole;
}
