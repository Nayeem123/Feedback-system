package feedback_system.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import feedback_system.constants.AppConstants;
import feedback_system.dto.*;
import feedback_system.entity.*;
import feedback_system.helper.PrepairResponse;
import feedback_system.repository.*;
import feedback_system.utility.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    @Autowired
    private FeedbackCategoriesRepo feedbackCategoriesRepo;
    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired private UserRepo userRepo;
    @Autowired private RoleRepo roleRepo;
    @Autowired private CategoryRepository categoryRepository;

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
        System.out.println(requestedUserRole.getRoles());
        System.out.println(user.getRoles());
        if(user.getRoles().contains("ROLE_SUPPORT")) {

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
        data.put("anonymous", feedback.isAnonymous());


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

    @Override
    public ApiResponse saveCategoryForm(CategoryDto categoryDto) {

        ApiResponse apiResponse = new ApiResponse();
        Category category = new Category();
        category.setCategory(categoryDto.getCategory());

        List<Question> questions = categoryDto.getQuestions().stream().map(q -> {
            Question question = new Question();
            question.setCategory(category);
            System.out.println(q.getIsAnonymous());

            question.setAnonymous(q.getIsAnonymous().equalsIgnoreCase("true"));

            question.setQuestion(q.getQuestion());
            question.setResponseType(q.getResponseType());
            question.setNoOfOptions(q.getNoOfOptions());

            List<OptionData> options = q.getOptionsData().stream().map(o -> {
                OptionData option = new OptionData();
                option.setName(o.getName());
                option.setQuestion(question);
                return option;
            }).collect(Collectors.toList());

            question.setOptionsData(options);
            return question;
        }).collect(Collectors.toList());

        category.setQuestions(questions);
        categoryRepository.save(category);


        apiResponse.setMessage("SUCCESS");
        apiResponse.setError(false);
        apiResponse.setData(categoryDto);
        return apiResponse;
    }

    @Override
    public ApiResponse getCategoryForm(String username, String categoryName) {

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

        Category category = categoryRepository.findByCategory(categoryName);
        if (category == null)  {
            apiResponse.setMessage("Data not found for : "+categoryName);
            apiResponse.setError(true);
            return apiResponse;
        }
        System.out.println(category.getCategory());
        CategoryDto  categoryDto = new CategoryDto();
        categoryDto.setCategory(category.getCategory());

        List<QuestionResponse> questions = category.getQuestions().stream().map(question -> {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setId(question.getId());
            questionResponse.setQuestion(question.getQuestion());
            questionResponse.setResponseType(question.getResponseType());
            questionResponse.setNoOfOptions(question.getOptionsData().size());
            System.out.println( " 2222 " + question.isAnonymous());
            questionResponse.setAnonymous(question.isAnonymous()); // Assuming anonymous is always false
            questionResponse.setOptionsData(question.getOptionsData().stream().map(option -> {
                OptionDataDto optionData = new OptionDataDto();
                optionData.setName(option.getName());
                return optionData;
            }).collect(Collectors.toList()));

            return questionResponse;
        }).collect(Collectors.toList());

        categoryDto.setQuestionResponses(questions);
        apiResponse.setError(false);
        apiResponse.setMessage("success");
        apiResponse.setData(categoryDto);
        return apiResponse;
    }
    @Override
    public ResponseEntity<byte[]> feedbackDownload(Long feedbackId) {
        {
            try {
                Optional<Feedback> feedback = fetchFeedbackData(feedbackId);
                List<FeedbackDto> feedbackList = new ArrayList<>();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A4);

                // Set font
                PdfFont titleFont = PdfFontFactory.createFont("Helvetica-Bold");
                PdfFont normalFont = PdfFontFactory.createFont("Helvetica");

                // Add Title
                Paragraph title = new Paragraph("Feedback Report")
                        .setFont(titleFont)
                        .setFontSize(18)
                        .setFontColor(ColorConstants.BLUE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(title);

//                for (FeedbackDto feedback : feedbackList) {
                    // Section Header
                    document.add(new Paragraph("Feedback Details")
                            .setFont(titleFont)
                            .setFontSize(14)
                            .setFontColor(ColorConstants.BLACK)
                            .setUnderline()
                            .setMarginBottom(10));
                    if (feedback.isPresent()){
                        // Feedback details with spacing
                        document.add(new Paragraph("Feedback ID: " + feedback.get().getId()).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Username: " + feedback.get().getUsername()).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Category: " + feedback.get().getCategoryName()).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Status: " + feedback.get().getStatus()).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Priority: " + feedback.get().getPriority()).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Anonymous: " + (feedback.get().isAnonymous() ? "Yes" : "No")).setFont(normalFont).setFontSize(12));
                        document.add(new Paragraph("Remarks: " + feedback.get().getRemarks()).setFont(normalFont).setFontSize(12));
                    }


                    // Add space
                    document.add(new Paragraph("\n"));

                    // Table for Question-Answer List
                    Table table = new Table(UnitValue.createPercentArray(new float[]{3, 5})).useAllAvailableWidth();
                    table.setMarginBottom(20);

                    // Add Table Header
                    table.addHeaderCell(new Cell().add(new Paragraph("Question").setFont(titleFont).setFontSize(12).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.DARK_GRAY)
                            .setTextAlignment(TextAlignment.CENTER));

                    table.addHeaderCell(new Cell().add(new Paragraph("Answer").setFont(titleFont).setFontSize(12).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.DARK_GRAY)
                            .setTextAlignment(TextAlignment.CENTER));

                    // Add Table Rows
                    for (QuestionAnswer qa : feedback.get().getQuestionAnswerList()) {
                        table.addCell(new Cell().add(new Paragraph(qa.getQuestion()).setFont(normalFont).setFontSize(11)));
                        table.addCell(new Cell().add(new Paragraph(qa.getAnswer()).setFont(normalFont).setFontSize(11)));
                    }

                    document.add(table);
                //}

                document.close();

                // Set response headers for download
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=feedback.pdf");

                return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("Error generating PDF: " + e.getMessage()).getBytes());
            }
        }
    }
        private Optional<Feedback> fetchFeedbackData(Long feedbackId) {
        Optional<Feedback> feedback= feedbackRepo.findById(feedbackId);
        if(!feedback.isPresent()){
            System.out.println("Feedback not found for  :"+feedbackId);
        }

        return feedback;
    }
}
