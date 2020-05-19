package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.ResetPasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordTokenValidationService {

    public void validateToken(ResetPasswordToken persistedToken, String token) {
        if (persistedToken == null || !isTokenMatched(persistedToken.getToken(), token)) {
            throw new CustomException("Invalid token.", HttpStatus.BAD_REQUEST);
        }
        if (isTokenExpired(persistedToken)) {
            throw new CustomException("Token is expired.", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isTokenExpired(ResetPasswordToken token) {
        return token.getExpiryDate().before(new Date());
    }

    private boolean isTokenMatched(String persistedToken, String token) {
        return persistedToken.equals(token);
    }}
