package feedback_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private String isAnonymous;
    private String question;
    private String responseType;
    private Integer noOfOptions;
    private List<OptionDataDto> optionsData;
}
