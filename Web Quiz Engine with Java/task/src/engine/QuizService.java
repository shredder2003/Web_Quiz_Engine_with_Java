package engine;

import engine.security.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final CurrentUser currentUser;
    private final Mapper mapper;
    private final UserQuizCompletedRepository userQuizCompletedRepository;

    public Page<QuizDTO> QuizDTOfindAll(Pageable pageable) {
        Page<Quiz> page = quizRepository.findAll(pageable);
        return new PageImpl<>(page.getContent().stream().map(mapper::convertQuizToDTO).toList(), pageable, page.getTotalElements());
    }

    public Page<UserQuizCompletedDTO> UserQuizCompletedDTOfindAll(Pageable pageable) {
        //Page<UserQuizCompleted> page = userQuizCompletedRepository.findAll(pageable);
        Page<UserQuizCompleted> page = userQuizCompletedRepository.findByEmailIgnoreCase(currentUser.get().getUsername(),pageable);
        return new PageImpl<>(page.getContent().stream().map(mapper::convertUserQuizCompletedToDTO).toList(), pageable, page.getTotalElements());
    }

    public Object getQuiz(Integer page, String id) {
        log.info("getQuiz(+) id={}, page={}",id,page);
        if(! currentUser.Authenticated() )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        Object result;
        if(id != null && ! id.isEmpty()){
            try {
                Quiz quiz = quizRepository.getReferenceById(Integer.valueOf(id));
                QuizDTO quizDTO = QuizDTO.builder()
                        .ID(quiz.getId())
                        .title(quiz.getTitle())
                        .text(quiz.getText())
                        .options(quiz.getOptions())
                        .build();
                //Pageable pageable = PageRequest.of(0, 10);
                //result = new PageImpl<>(List.of(quizDTO), pageable, 1);
                result = quizDTO;
            }catch(Exception e){
                log.info("getQuiz(-) id={} e={}",id,e.getMessage());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"not found!");
            }
        }else{
            /*result = quizRepository.findAll().stream().map( quiz ->
                            QuizDTO.builder()
                                    .ID(quiz.getId())
                                    .title(quiz.getTitle())
                                    .text(quiz.getText())
                                    .options(quiz.getOptions())
                                    .build()
                    ).toList();*/
            Pageable pageable = PageRequest.of(page, 10);
            result = this.QuizDTOfindAll(pageable);
        }
        log.info("getQuiz(-) id={} result={}",id,result);
        return result;
    }

    public Object getCompleted(int page) {
        log.info("getCompleted(+) page={}", page);
        if (!currentUser.Authenticated())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        Object result;
        Pageable pageable = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        result = this.UserQuizCompletedDTOfindAll(pageable);
        log.info("getCompleted(-) page={} result={}",page,result);
        return result;
    }

    public AnswerResult postQuiz(Integer id, Answer answer){
        log.info("postQuiz(+) id={} answer={}",id,answer);
        if(! currentUser.Authenticated() )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        Quiz quiz = quizRepository.getById(id);
        AnswerResult answerResult;
        if( (quiz.getAnswer()==null && answer.getAnswer()==null )
                || ( (quiz.getAnswer()!=null && quiz.getAnswer().length==0) && answer.getAnswer()==null )
                || ( (answer.getAnswer()!=null && answer.getAnswer().length==0) && quiz.getAnswer()==null )
                || Arrays.equals(quiz.getAnswer(), answer.getAnswer())
        ){
            answerResult = new AnswerResult(true,"Congratulations, you're right!");
            UserQuizCompleted userQuizCompleted = UserQuizCompleted.builder()
                    .email(currentUser.get().getUser().getEmail())
                    .quizID(id)
                    .completedAt(LocalDateTime.now())
                    .build();
            log.info("postQuiz userQuizCompleted={} ",userQuizCompleted);
            userQuizCompletedRepository.save(userQuizCompleted);
        }else{
            answerResult = new AnswerResult(false,"Wrong answer! Please, try again.");
        }
        log.info("postQuiz(-) id={} answer={} answerResult={}",id,answer,answerResult);
        return answerResult;
    }

    public QuizDTO createQuiz(Quiz quiz){
        log.info("createQuiz(+) quiz={}",quiz);
        if(! currentUser.Authenticated() )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        quiz.setAuthor(currentUser.get().getUsername());
        quiz = quizRepository.save(quiz);
        QuizDTO result = QuizDTO.builder()
                .ID(quiz.getId())
                .title(quiz.getTitle())
                .text(quiz.getText())
                .options(quiz.getOptions())
                .build();
        log.info("createQuiz(-) quiz={} result={}",quiz,result);
        return result;
    }

    public void deleteQuiz(int id) {
        log.info("createQuiz(+) id={}", id);
        if (!currentUser.Authenticated())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        Optional<Quiz> quiz = quizRepository.findById(id);
        if(quiz.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND");
        }else if(quiz.get().getAuthor().equals(currentUser.get().getUsername())){
            quizRepository.deleteById(id);
            quizRepository.flush();
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "NO_CONTENT");
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN");
        }
    }

}
