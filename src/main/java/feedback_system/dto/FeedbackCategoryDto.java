package feedback_system.dto;

import feedback_system.entity.FeedbackCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackCategoryDto {

    private String categoryName;
    private String categoryDesc;
    public static FeedbackCategoryDto getFeedbackCategoryDto(FeedbackCategory feedbackCategory){
        FeedbackCategoryDto feedbackCategoryDto = new FeedbackCategoryDto();
        feedbackCategoryDto.setCategoryName(feedbackCategory.getCategoryName());
        feedbackCategoryDto.setCategoryDesc(feedbackCategory.getCategoryDesc());
        return feedbackCategoryDto;
    }
}
