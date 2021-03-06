package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.AuthenticationRequest;
import lk.apiit.eea.stylouse.dto.request.UserRequest;
import lk.apiit.eea.stylouse.dto.response.AuthenticationResponse;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.security.JwtUtil;
import lk.apiit.eea.stylouse.security.UserDetailsImpl;
import lk.apiit.eea.stylouse.security.UserDetailsServiceImpl;
import lk.apiit.eea.stylouse.services.ResetPasswordService;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ResetPasswordService resetPasswordService;
    @Value("${app.token.validation}")
    private String tokenValidation;

    public AuthenticationController(
            UserDetailsServiceImpl userDetailsService,
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            UserService userService,
            ResetPasswordService resetPasswordService) {
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            return ok(authToken(request.getUsername()));
        } catch (BadCredentialsException ex) {
            throw new CustomException("Invalid username or password.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        User user = userService.createUser(request.getUser());
        return ok(authToken(user.getEmail()));
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        resetPasswordService.createResetPasswordToken(user);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Reset password requested.");
        return ok(responseMap);
    }

    @PostMapping("/reset-password-confirmation")
    public ResponseEntity<?> resetPasswordConfirmation(@RequestBody AuthenticationRequest request) {
        String token = request.getPassword();
        return ok(resetPasswordService.confirmPasswordReset(request.getUsername(), token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AuthenticationRequest authRequest) {
        resetPasswordService.resetPassword(authRequest);
        return login(authRequest);
    }

    private AuthenticationResponse authToken(String email) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
        String jwt = jwtUtil.generateToken(userDetails);
        String userRole = userDetails.getUserRole();
        return new AuthenticationResponse(userDetails.getUsername(), tokenValidation, jwt, userRole);
    }
}
