package feedback_system.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {
    private String question;
    private String answer;

}