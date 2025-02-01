package feedback_system.service;

import feedback_system.constants.AppConstants;
import feedback_system.dto.DashboardDTO;
import feedback_system.dto.FeedbackCategoryDto;
import feedback_system.dto.FeedbackDto;
import feedback_system.dto.QuestionAnswer;
import feedback_system.entity.Feedback;
import feedback_system.entity.FeedbackCategory;
import feedback_system.entity.Role;
import feedback_system.entity.User;
import feedback_system.helper.PrepairResponse;
import feedback_system.repository.FeedbackCategoriesRepo;
import feedback_system.repository.FeedbackRepo;
import feedback_system.repository.RoleRepo;
import feedback_system.repository.UserRepo;
import feedback_system.utility.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    @Autowired
    private FeedbackCategoriesRepo feedbackCategoriesRepo;
    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired private UserRepo userRepo;
    @Autowired private RoleRepo roleRepo;

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

    @Override
    public ApiResponse getDashboardData(String username) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepo.findByUsername(username);
        if (user == null)  {
            apiResponse.setMessage("User not found");
            apiResponse.setError(true);
            return apiResponse;
        }

        Role requestedUserRole = roleRepo.findByUserId(user.getId());
        if (requestedUserRole == null)  {
            apiResponse.setMessage("User doesn't have any role");
            apiResponse.setError(true);
            return apiResponse;
        }

        apiResponse.setError(false);
        apiResponse.setMessage("success");

        List<Feedback> feedbacks = feedbackRepo.findAll();

        if (feedbacks.isEmpty()){
            apiResponse.setMessage("No feedbacks found");
            apiResponse.setError(true);
            apiResponse.setData(new ArrayList<>());
            return apiResponse;
        }

        if(requestedUserRole.getRoles().contains("ROLE_SUPPORT")) {

            Map<String, DashboardDTO> processedCategories = new HashMap<>();
            feedbacks.stream().forEach(feedback -> {
                DashboardDTO dashboardDTO = new DashboardDTO();
                dashboardDTO.setFeedbackId(feedback.getId());
                dashboardDTO.setCategoryName(feedback.getCategoryName());

                if (processedCategories.containsKey(feedback.getCategoryName())) {
                    dashboardDTO = processedCategories.get(feedback.getCategoryName());
                }

                if(feedback.getStatus().equalsIgnoreCase("OPEN")) {
                    dashboardDTO.setOpenCount(dashboardDTO.getOpenCount() + 1);
                } else if (feedback.getStatus().equalsIgnoreCase("RESOLVED")) {
                    dashboardDTO.setResolvedCount(dashboardDTO.getResolvedCount() + 1);
                } else if (feedback.getStatus().equalsIgnoreCase("INPROGRESS")) {
                    dashboardDTO.setInProgressCount(dashboardDTO.getInProgressCount() + 1);
                }

                processedCategories.put(feedback.getCategoryName(), dashboardDTO);

            });
            apiResponse.setData(new ArrayList<>(processedCategories.values()));

        } else if (requestedUserRole.getRoles().contains("ROLE_ADMIN")) {

        } else {
            apiResponse.setData(new ArrayList<>());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse getFeedbackDetails(String feedbackName) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, Object> data = new HashMap<>();
        data.put("feedbackList", new ArrayList<>());
        data.put("openCount", 0);
        data.put("resolvedCount", 0);
        data.put("inprogressCount", 0);
        data.put("urgentCount", 0);
        data.put("mediumCount", 0);
        data.put("lowCount", 0);

        apiResponse.setError(false);
        apiResponse.setMessage("success");

        List<Feedback> feedbacks = feedbackRepo.findAllByCategoryName(feedbackName);

        if (feedbacks.isEmpty()){
            apiResponse.setMessage("No feedbacks found");
            apiResponse.setError(true);
            apiResponse.setData(data);
            return apiResponse;
        }
        List<Map<String, Object>> feedbackDetails = new ArrayList<>();

        feedbacks.stream().forEach(feedback -> {
            Map<String, Object> feedbackDetail = new HashMap<>();

            if(feedback.getStatus().equalsIgnoreCase("OPEN")) {
                data.put("openCount", ((Integer) data.get("openCount")) + 1);
            } else if (feedback.getStatus().equalsIgnoreCase("RESOLVED")) {
                data.put("resolvedCount", ((Integer) data.get("resolvedCount")) + 1);
            } else if (feedback.getStatus().equalsIgnoreCase("INPROGRESS")) {
                data.put("inprogressCount", ((Integer) data.get("inprogressCount")) + 1);
            }

            if(feedback.getPriority().equalsIgnoreCase("URGENT")) {
                data.put("urgentCount", ((Integer) data.get("urgentCount")) + 1);
            } else if (feedback.getPriority().equalsIgnoreCase("MEDIUM")) {
                data.put("mediumCount", ((Integer) data.get("mediumCount")) + 1);
            } else if (feedback.getPriority().equalsIgnoreCase("Low")) {
                data.put("lowCount", ((Integer) data.get("lowCount")) + 1);
            }

            feedbackDetail.put("id", feedback.getId());
            feedbackDetail.put("categoryName", feedback.getCategoryName());
            feedbackDetail.put("priority", feedback.getPriority());
            feedbackDetail.put("status", feedback.getStatus());
            feedbackDetail.put("anonymous", feedback.isAnonymous());
            feedbackDetail.put("username", feedback.getUsername());

            feedbackDetails.add(feedbackDetail);

        });

        data.put("feedbackDetails", feedbackDetails);
        apiResponse.setData(data);
        return apiResponse;
    }

    @Override
    public ApiResponse getFeedbackDetail(Long feedbackId) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, Object> data = new HashMap<>();


        Feedback feedback = feedbackRepo.findById(feedbackId).orElse(null);
        if (feedback == null) {
            apiResponse.setMessage("No feedback found");
            apiResponse.setError(true);
            apiResponse.setData(data);
            return apiResponse;
        }

        User user = userRepo.findByUsername(feedback.getUsername());

        data.put("id", feedback.getId());
        data.put("categoryName", feedback.getCategoryName());
        data.put("username", feedback.getUsername());
        data.put("fullName", user != null ? user.getFullname() : null);
        data.put("feedback", feedback.getQuestionAnswerList());
        data.put("comment", feedback.getRemarks());


        apiResponse.setMessage("success");
        apiResponse.setError(false);

        apiResponse.setData(data);
        return apiResponse;
    }

    @Override
    public ApiResponse updateFeedback(FeedbackDto feedbackDto) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(feedbackDto);

        if (feedbackDto.getId() == null || feedbackDto.getId() == 0
        || feedbackDto.getStatus() == null || feedbackDto.getStatus().isEmpty()
        || feedbackDto.getRemarks() == null || feedbackDto.getRemarks().isEmpty()) {

            apiResponse.setMessage("Invalid parameter field/value found");
            apiResponse.setError(true);
            return apiResponse;
        }

        Feedback feedback = feedbackRepo.findById(feedbackDto.getId()).orElse(null);
        if (feedback == null) {
            apiResponse.setMessage("No feedback found");
            apiResponse.setError(true);
            return apiResponse;
        }

        feedback.setStatus(feedbackDto.getStatus());
        feedback.setRemarks(feedbackDto.getRemarks());
        feedbackRepo.save(feedback);


        feedbackDto.setCategoryName(feedback.getCategoryName());
        apiResponse.setData(feedbackDto);

        apiResponse.setError(false);
        apiResponse.setMessage("success");
        return apiResponse;
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
