package ru.itis.inf304.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.inf304.config.MailConfig;
import ru.itis.inf304.entity.User;
import ru.itis.inf304.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConfig mailConfig;

    public void registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setIsEmailConfirmed(false);
        userRepository.save(user);

        sendVerificationEmail(user);
    }
    public void confirmEmailByCode(String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));
        user.setIsEmailConfirmed(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(User::fromUser)
                .collect(Collectors.toList());
    }

    private void sendVerificationEmail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailConfig.getFrom(), mailConfig.getSender());
            helper.setTo(user.getEmail());
            helper.setSubject(mailConfig.getSubject());

            String content = mailConfig.getContent()
                    .replace("{name}", user.getUsername())
                    .replace("{url}", "http://localhost:8080/user/confirm?code=" + user.getVerificationCode());

            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}