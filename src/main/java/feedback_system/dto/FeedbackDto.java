package feedback_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import feedback_system.entity.Feedback;
import feedback_system.entity.FeedbackCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private Long id;
    private String username;
    private String categoryName;
    private String status;
    private Map<String,String> questionAnswermap;
    private boolean anonymous;
    private String priority;
    private String remarks;
    private String action;

    public static FeedbackDto getFeedbackDto(Feedback feedback){
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(feedback.getId());
        feedbackDto.setUsername(feedback.getUsername());
        feedbackDto.setCategoryName(feedback.getCategoryName());
        feedbackDto.setStatus(feedback.getStatus());
        feedbackDto.setQuestionAnswermap(feedback.getQuestionAnswermap());
        feedbackDto.setAnonymous(feedback.isAnonymous());
        feedbackDto.setPriority(feedback.getPriority());
        feedbackDto.setRemarks(feedback.getRemarks());
        return feedbackDto;
    }
}
