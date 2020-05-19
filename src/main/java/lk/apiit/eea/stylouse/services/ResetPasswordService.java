package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.dto.request.AuthenticationRequest;
import lk.apiit.eea.stylouse.dto.response.AuthenticationResponse;
import lk.apiit.eea.stylouse.mail.ResetPasswordMailService;
import lk.apiit.eea.stylouse.models.ResetPasswordToken;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.ResetPasswordTokenRepository;
import lk.apiit.eea.stylouse.security.JwtUtil;
import lk.apiit.eea.stylouse.security.UserDetailsImpl;
import lk.apiit.eea.stylouse.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {
    @Value("${password.token.expiration}")
    long expireIn;

    @Value("${app.token.validation}")
    private String tokenValidation;

    private final ResetPasswordTokenRepository passwordTokenRepository;
    private final ResetPasswordMailService mailService;
    private final PasswordTokenValidationService tokenValidationService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ResetPasswordService(
            ResetPasswordTokenRepository passwordTokenRepository,
            ResetPasswordMailService mailService,
            PasswordTokenValidationService tokenValidationService,
            UserService userService,
            UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.passwordTokenRepository = passwordTokenRepository;
        this.mailService = mailService;
        this.tokenValidationService = tokenValidationService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void createResetPasswordToken(User user) {
        ResetPasswordToken oldToken = passwordTokenRepository.findByUser(user);
        if (oldToken != null) {
            passwordTokenRepository.delete(oldToken);
        }

        ResetPasswordToken token = passwordTokenRepository.save(new ResetPasswordToken(user, expireIn));
        new Thread(() -> mailService.sendMail(user, "Reset Password", token)).start();
    }

    public AuthenticationResponse confirmPasswordReset(String username, String token) {
        User user = userService.getUserByEmail(username);
        ResetPasswordToken persistedToken = getTokenByUser(user);
        tokenValidationService.validateToken(persistedToken, token);
        passwordTokenRepository.delete(persistedToken);
        return authToken(username);
    }

    private ResetPasswordToken getTokenByUser(User user) {
        return passwordTokenRepository.findByUser(user);
    }

    private AuthenticationResponse authToken(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);
        String userRole = userDetails.getUserRole();
        return new AuthenticationResponse(userDetails.getUsername(), tokenValidation, jwt, userRole);
    }

    public void resetPassword(AuthenticationRequest request) {
        userService.savePassword(request.getUsername(), request.getPassword());
    }
}