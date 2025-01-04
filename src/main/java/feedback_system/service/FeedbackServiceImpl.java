package feedback_system.service;

import feedback_system.constants.AppConstants;
import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.entity.FeedbackCategory;
import feedback_system.helper.PrepairResponse;
import feedback_system.repository.FeedbackCategoriesRepo;
import feedback_system.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    private FeedbackCategoriesRepo feedbackCategoriesRepo;

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
}
