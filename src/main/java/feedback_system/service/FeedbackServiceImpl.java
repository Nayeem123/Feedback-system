package feedback_system.service;

import feedback_system.constants.AppConstants;
import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.dto.FeedbackDto;
import feedback_system.dto.QuestionAnswer;
import feedback_system.entity.Feedback;
import feedback_system.entity.FeedbackCategory;
import feedback_system.helper.PrepairResponse;
import feedback_system.repository.FeedbackCategoriesRepo;
import feedback_system.repository.FeedbackRepo;
import feedback_system.utility.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    @Autowired
    private FeedbackCategoriesRepo feedbackCategoriesRepo;
    @Autowired
    private FeedbackRepo feedbackRepo;

    @Override
    public ApiResponse showFeedbackCategories() {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        List<FeedbackCategory> feedbackCategoryList =feedbackCategoriesRepo.findAll();
        if(!feedbackCategoryList.isEmpty()){
            List<FeedbackCategoryDto> feedbackCategoryDtoList = new ArrayList<>();

            feedbackCategoryList.stream().forEach(feedbackCategory -> feedbackCategoryDtoList
                    .add(FeedbackCategoryDto.getFeedbackCategoryDto(feedbackCategory)));
            apiResponse.setFeedbackCategoryList(feedbackCategoryDtoList);
            apiResponse.setMessage(AppConstants.FEEDBACK_CATEGORIES_FETCHED);
            return prepairResponse.setSuccessResponse(apiResponse);
        }
        apiResponse.setMessage(AppConstants.FEEDBACK_CATEGORIES_NOT_PRESENT);
        return prepairResponse.setApiResponseFail(apiResponse);
    }
    @Override
    public ApiResponse addFeedbackCategories(FeedbackCategoryDto feedbackCategoryDto) {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        FeedbackCategory feedbackCategory = feedbackCategoriesRepo.findByCategoryName(feedbackCategoryDto.getCategoryName());
         if(feedbackCategory == null){
             FeedbackCategory feedbackCategory1 = saveCategory(feedbackCategoryDto);
             //FeedbackCategoryDto feedbackCategoryDto1 = new FeedbackCategoryDto();
             apiResponse.setFeedbackCategoryDto(feedbackCategoryDto);
             apiResponse.setMessage(AppConstants.FEEDBACK_CATEGORY_ADDED);
             return prepairResponse.setSuccessResponse(apiResponse);
         }
         apiResponse.setMessage(AppConstants.FEEDBACK_CATEGORY_NAME_EXISTS);
        return prepairResponse.setApiResponseFail(apiResponse);
    }

    private FeedbackCategory saveCategory(FeedbackCategoryDto feedbackCategoryDto){
        System.out.println(feedbackCategoryDto.getCategoryName());
        FeedbackCategory feedbackCategory = new FeedbackCategory();
        feedbackCategory.setCategoryName(feedbackCategoryDto.getCategoryName());
        feedbackCategory.setCategoryDesc(feedbackCategoryDto.getCategoryDesc());
        return feedbackCategoriesRepo.save(feedbackCategory);
    }
    @Override
    public ApiResponse submitFeedback(FeedbackDto feedbackDto){
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        Feedback feedback = saveFeedback(feedbackDto);
        apiResponse.setFeedbackDto(feedbackDto);
        apiResponse.setMessage(AppConstants.FEEDBACK_SUBMITTED);
        return prepairResponse.setSuccessResponse(apiResponse);
    }


    private Feedback saveFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setUsername(feedbackDto.getUsername());
        feedback.setCategoryName(feedbackDto.getCategoryName());
        feedback.setStatus(feedbackDto.getStatus());
        //feedback.setQuestionAnswermap(feedbackDto.getQuestionAnswermap());
        List<QuestionAnswer> questionAnswerList = new ArrayList<>();
        if (feedbackDto.getQuestionAnswermap() != null) {
            for (Map.Entry<String, String> entry : feedbackDto.getQuestionAnswermap().entrySet()) {
                questionAnswerList.add(new QuestionAnswer(entry.getKey(), entry.getValue()));
            }
        }System.out.println(feedbackDto.getQuestionAnswermap());
        System.out.println(" ======================== List below ");
        System.out.println(questionAnswerList);
        feedback.setQuestionAnswerList(questionAnswerList);
        feedback.setAnonymous(feedbackDto.isAnonymous());
        feedback.setPriority(feedbackDto.getPriority());
        feedback.setRemarks(feedbackDto.getRemarks());
        return feedbackRepo.save(feedback);
    }
    @Override
    public ApiResponse showFeedback(String username) {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        //List<Feedback> feedbacks = feedbackRepo.findAll();
        System.out.println(" service " + username.toString());
        List<Feedback> feedbacks = feedbackRepo.findAllByUsername(username);
        System.out.println(feedbacks);
        if (!feedbacks.isEmpty()){
            List<FeedbackDto> feedbackDtoList = new ArrayList<>();
            feedbacks.stream().forEach(feedback ->
                    feedbackDtoList.add(FeedbackDto.getFeedbackDto(feedback)));
            apiResponse.setFeedbackDtoList(feedbackDtoList);
            apiResponse.setMessage(AppConstants.FEEDBACK_GET);
            return prepairResponse.setSuccessResponse(apiResponse);
        }
        apiResponse.setMessage(AppConstants.FEEDBACK_NOT_FOUND);
        return prepairResponse.setSuccessResponse(apiResponse);
    }

    @Override
    public ApiResponse showAllFeedback() {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        List<Feedback> feedbacks = feedbackRepo.findAll();

        if (!feedbacks.isEmpty()){
            List<FeedbackDto> feedbackDtoList = new ArrayList<>();
            feedbacks.stream().forEach(feedback ->
                    feedbackDtoList.add(FeedbackDto.getFeedbackDto(feedback)));
            apiResponse.setFeedbackDtoList(feedbackDtoList);
            apiResponse.setMessage(AppConstants.FEEDBACK_GET_ALL);
            return prepairResponse.setSuccessResponse(apiResponse);
        }
        apiResponse.setMessage(AppConstants.FEEDBACK_NOT_FOUND);
        return prepairResponse.setSuccessResponse(apiResponse);
    }

    @Override
    public ApiResponse resolveFeedback(FeedbackDto feedbackDto) {
        ApiResponse apiResponse = new ApiResponse();
        PrepairResponse prepairResponse = new PrepairResponse();
        //System.out.println(" id to resole " + feedbackDto.getId());
        try{
            Feedback existingFeedback = feedbackRepo.findById(feedbackDto.getId())
                    .orElseThrow(()-> new RuntimeException("Feedback not found"));
            updateFeedback(existingFeedback,feedbackDto,apiResponse);

        }catch (Exception exception){
            log.error("Error while setting feedback status");
        }
       return prepairResponse.setSuccessResponse(apiResponse);
    }

    private void updateFeedback(Feedback existingFeedback,FeedbackDto feedbackDto,ApiResponse apiResponse) {

        if (feedbackDto != null && feedbackDto.getAction().equalsIgnoreCase(AppConstants.RESOLVED)){
            existingFeedback.setRemarks(feedbackDto.getRemarks());
            existingFeedback.setStatus(feedbackDto.getStatus());
            apiResponse.setMessage(AppConstants.FEEDBACK_RESOLVED);
            feedbackRepo.save(existingFeedback);
        }
        else if (feedbackDto != null && feedbackDto.getAction().equalsIgnoreCase(AppConstants.INPROGRESS)){
            existingFeedback.setRemarks(feedbackDto.getRemarks());
            existingFeedback.setStatus(feedbackDto.getStatus());
            apiResponse.setMessage(AppConstants.FEEDBACK_INPROGRESS);
            feedbackRepo.save(existingFeedback);
        }
    }


}
