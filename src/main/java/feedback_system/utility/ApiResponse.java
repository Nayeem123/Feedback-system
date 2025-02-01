package feedback_system.utility;

import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.dto.FeedbackDto;
import feedback_system.dto.UserDto;
import feedback_system.entity.FeedbackCategory;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean error;
    private String message;
    private UserDto user;
    private List<UserDto> userList;
    private FeedbackCategoryDto feedbackCategoryDto;
    private List<FeedbackCategoryDto> feedbackCategoryList;
    private FeedbackDto feedbackDto;
    private List<FeedbackDto> feedbackDtoList;
    private Object data;
}
