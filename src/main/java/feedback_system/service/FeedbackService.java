package feedback_system.service;

import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.utility.ApiResponse;

public interface FeedbackService {
    ApiResponse showFeedbackCategories();
    ApiResponse addFeedbackCategories(FeedbackCategoryDto feedbackCategoryDto);
}
