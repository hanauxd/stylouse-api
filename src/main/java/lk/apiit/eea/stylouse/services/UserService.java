package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.exceptions.CustomException;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException("User not found.", HttpStatus.NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found.", HttpStatus.NOT_FOUND));
    }

    public User createUser(User user) {
        boolean isPresent = userRepository.findByEmail(user.getEmail()).isPresent();
        if (isPresent) throw new CustomException("Email address already exist.", HttpStatus.BAD_REQUEST);
        return userRepository.save(user);
    }

    public User savePassword(String username, String password) {
        User user = getUserByEmail(username);
        user.setPassword(hashPassword(password));
        return userRepository.save(user);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
