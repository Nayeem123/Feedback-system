package feedback_system.controller;

import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.service.FeedbackService;
import feedback_system.utility.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

    private FeedbackService feedbackService;
    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }
    @GetMapping("/feedback/fetch-categories")
    public ApiResponse showFeedbackCategories(){
        return feedbackService.showFeedbackCategories();
    }

    @PostMapping("/feedback/add-categories")
    public ApiResponse addFeedbackCategories(@RequestBody FeedbackCategoryDto feedbackCategoryDto){
        return feedbackService.addFeedbackCategories(feedbackCategoryDto);
    }

}
