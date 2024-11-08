package engine;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    QuizDTO convertQuizToDTO(Quiz quiz) {
        return modelMapper.map(quiz, QuizDTO.class);
    }

    UserQuizCompletedDTO convertUserQuizCompletedToDTO(UserQuizCompleted userQuizCompleted) {
        return UserQuizCompletedDTO.builder()
                .id(userQuizCompleted.getQuizID())
                .completedAt(userQuizCompleted.getCompletedAt())
                .build();
    }

}
