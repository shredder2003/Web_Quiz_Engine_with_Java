package engine.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user){
        log.info("register(+) user={}",user);
        if(userRepository.findUserByEmailIgnoreCase(user.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already exists!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        log.info("register(-) newUser={}",newUser);
        return newUser;
    }

}
