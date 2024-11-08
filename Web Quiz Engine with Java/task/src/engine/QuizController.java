package engine;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/quizzes")
@AllArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping(value = {"/{id}", ""})
    public Object getQuiz(@PathVariable(required = false) String id, @RequestParam(required = false) Integer page) {
        return quizService.getQuiz(page, id);
    }

    @GetMapping("/completed")
    public Object getCompleted(@RequestParam int page) {
        return quizService.getCompleted(page);
    }

    @PostMapping("/{id}/solve")
    //public AnswerResult postQuiz(@PathVariable Integer id, @RequestBody @Valid Answer answer) {
    public AnswerResult postQuiz(@PathVariable Integer id, @RequestBody(required = false) @Valid Answer answer) {
        return quizService.postQuiz(id, answer);
    }

    @PostMapping()
    public QuizDTO postQuiz(@RequestBody @Valid Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @DeleteMapping(value = {"/{id}", ""})
    public void deleteQuiz(@PathVariable int id) {
        quizService.deleteQuiz(id);
    }

}
