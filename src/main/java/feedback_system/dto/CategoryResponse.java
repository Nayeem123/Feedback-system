package feedback_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private String category;
    private List<QuestionResponse> questions;
}
