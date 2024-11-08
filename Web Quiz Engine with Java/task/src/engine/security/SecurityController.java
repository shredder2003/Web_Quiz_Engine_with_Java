package engine.security;

import engine.Quiz;
import engine.QuizDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping()
    public User register(@RequestBody @Valid User user) {
        return securityService.register(user);
    }

}
