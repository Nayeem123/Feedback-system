package feedback_system.dto;

import feedback_system.entity.Question;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CategoryDto {
    private String category;
    private List<QuestionDto> questions;
    List<QuestionResponse> questionResponses;
}
