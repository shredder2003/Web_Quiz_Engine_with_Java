package engine;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AnswerResult {
    private Boolean success;
    private String feedback;
}
