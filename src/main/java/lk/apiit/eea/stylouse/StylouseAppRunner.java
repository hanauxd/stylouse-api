package lk.apiit.eea.stylouse;

import lk.apiit.eea.stylouse.dto.request.UserRequest;
import lk.apiit.eea.stylouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StylouseAppRunner implements CommandLineRunner {
    private UserRepository userRepository;

    @Autowired
    public StylouseAppRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        long adminCount = userRepository.countByRole("ROLE_ADMIN");
        if (adminCount == 0) {
            UserRequest userRequest = new UserRequest("ROLE_ADMIN", "Stylouse", "Admin", "0754442258", "admin@gmail.com", "pass");
            userRepository.save(userRequest.getUser());
        }
    }
}
