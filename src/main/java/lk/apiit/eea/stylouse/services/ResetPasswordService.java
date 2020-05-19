package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.mail.ResetPasswordMailService;
import lk.apiit.eea.stylouse.models.ResetPasswordToken;
import lk.apiit.eea.stylouse.models.User;
import lk.apiit.eea.stylouse.repositories.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {
    private final ResetPasswordTokenRepository passwordTokenRepository;
    private final ResetPasswordMailService mailService;

    @Autowired
    public ResetPasswordService(
            ResetPasswordTokenRepository passwordTokenRepository,
            ResetPasswordMailService mailService) {
        this.passwordTokenRepository = passwordTokenRepository;
        this.mailService = mailService;
    }

    public void createResetPasswordToken(User user) {
        ResetPasswordToken token = passwordTokenRepository.save(new ResetPasswordToken(user));
        new Thread(() -> mailService.sendMail(user, "Reset Password", token)).start();
    }
}
