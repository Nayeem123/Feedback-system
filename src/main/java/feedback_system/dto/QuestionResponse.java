package feedback_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuestionResponse {
    private String question;
    private String responseType;
    private Integer noOfOptions;
    private List<OptionDataDto> optionsData;
    private boolean anonymous;
}
