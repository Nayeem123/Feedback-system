package feedback_system.controller;

import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.dto.FeedbackDto;
import feedback_system.entity.Feedback;
import feedback_system.service.FeedbackService;
import feedback_system.utility.ApiResponse;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/feedback/submit")
    public ApiResponse submitFeedback(@RequestBody FeedbackDto feedbackDto){
        System.out.println(feedbackDto.toString());
        return feedbackService.submitFeedback(feedbackDto);
    }
    @GetMapping("/feedback/show-feedback")
    public ApiResponse showFeedback(@RequestParam("username") String username){
        System.out.println(" ================= "+ username);
        return feedbackService.showFeedback(username);
    }

}
