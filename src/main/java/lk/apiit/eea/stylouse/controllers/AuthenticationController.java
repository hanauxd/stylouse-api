package lk.apiit.eea.stylouse.controllers;

import lk.apiit.eea.stylouse.dto.request.AuthenticationRequest;
import lk.apiit.eea.stylouse.dto.request.UserRequest;
import lk.apiit.eea.stylouse.dto.response.AuthenticationResponse;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.security.JwtUtil;
import lk.apiit.eea.stylouse.security.UserDetailsImpl;
import lk.apiit.eea.stylouse.security.UserDetailsServiceImpl;
import lk.apiit.eea.stylouse.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {
    @Value("${app.token.validation}")
    private String tokenValidation;
    private UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    public AuthenticationController(
            UserDetailsServiceImpl userDetailsService,
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            UserService userService
    ) {
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            String userRole = userDetails.getUserRole();
            return ResponseEntity.ok(new AuthenticationResponse(userDetails.getUsername(), tokenValidation, jwt, userRole));
        } catch (BadCredentialsException ex) {
            throw new CustomException("Invalid username or password.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping({"/register"})
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        User user = userService.createUser(request.getUser());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
