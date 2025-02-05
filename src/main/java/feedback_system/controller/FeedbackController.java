package feedback_system.controller;

import feedback_system.dto.CategoryDto;
import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.dto.FeedbackDto;
import feedback_system.entity.Feedback;
import feedback_system.entity.Role;
import feedback_system.entity.User;
import feedback_system.repository.RoleRepo;
import feedback_system.service.FeedbackService;
import feedback_system.service.UserService;
import feedback_system.service.UserServiceImpl;
import feedback_system.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    private final UserServiceImpl userServiceImpl;
    private UserService userService;

    @Autowired private RoleRepo roleRepo;

    private FeedbackService feedbackService;
    public FeedbackController(FeedbackService feedbackService, UserServiceImpl userServiceImpl, UserService userService){
        this.feedbackService = feedbackService;
        this.userServiceImpl = userServiceImpl;
        this.userService = userService;
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
        System.out.println(feedbackDto.getQuestionAnswermap());
        return feedbackService.submitFeedback(feedbackDto);
    }
    @GetMapping("/feedback/show-feedback")
    public ApiResponse showFeedback(@RequestParam("username") String username){
        System.out.println(" ================= "+ username);
        return feedbackService.showFeedback(username);
    }

    @GetMapping("/feedback/show-all-feedback")
    public ApiResponse showAllFeedback(){
        System.out.println(" ================= ");
        return feedbackService.showAllFeedback();
    }

    @PostMapping("/support/feedback/resolve")
    public ApiResponse resolveFeedback(@RequestBody FeedbackDto feedbackDto){
        System.out.println(feedbackDto.getId());
        return feedbackService.resolveFeedback(feedbackDto);
    }

    @GetMapping("/dashboard/{username}")
    public ApiResponse showAllFeedback(@PathVariable String username){
        System.out.println(" ================= ");

        return feedbackService.getDashboardData(username);
    }

    @GetMapping("/feedback/categories/{f_name}")
    public ApiResponse getFeedbackCategories(@PathVariable(name = "f_name") String feedbackName){
        System.out.println(feedbackName);

        return feedbackService.getFeedbackDetails(feedbackName);
    }

    @GetMapping("/feedback/submissions/{id}")
    public ApiResponse getFeedbackCategories(@PathVariable(name = "id") Long feedbackId){
        System.out.println(feedbackId);

        return feedbackService.getFeedbackDetail(feedbackId);
    }

    @PostMapping("/feedback/submissions/update")
    public ApiResponse updateFeedback(@RequestBody FeedbackDto feedbackDto){
        System.out.println(feedbackDto.getId());
        return feedbackService.updateFeedback(feedbackDto);
    }

    @PostMapping("/feedback/category/questions")
    public ApiResponse saveCategoryForm(@RequestBody CategoryDto categoryDto){
        System.out.println(categoryDto.getCategory());
        return feedbackService.saveCategoryForm(categoryDto);
    }

    @GetMapping("/feedback/category/fetch-questions-form")
    public ApiResponse getCategoryForm(@RequestParam("username") String username, @RequestParam("categoryName") String categoryName){
        System.out.println(username);
        return feedbackService.getCategoryForm(username,categoryName);
    }
}
