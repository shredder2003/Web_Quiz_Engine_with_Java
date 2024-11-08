package engine;

import org.springframework.stereotype.Component;


@Component
public class InitQuizDB {

    private final QuizRepository quizRepository;

    public InitQuizDB(QuizRepository quizRepository){
        this.quizRepository = quizRepository;
        //init();
    }

    public void init(){
        String[] questions = {"Robot","Tea leaf","Cup of coffee","Bug"};
        //Quiz quiz = new Quiz(1,"The Java Logo", "What is depicted on the Java logo?", questions,2);
        //quizRepository.save(quiz);
    }

}
