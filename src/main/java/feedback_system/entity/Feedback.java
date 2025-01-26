package feedback_system.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feedback_system.dto.QuestionAnswer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String categoryName;
    private String status;
    @Column(columnDefinition = "json")
    private String questionAnswer;
    private boolean anonymous;
    private String priority;
    private String remarks;

    @Transient
    private Map<String,String> questionAnswermap = new HashMap<>();
    @Transient
    private List<QuestionAnswer> questionAnswerList = new ArrayList<>();

    @PostLoad
    private void loadQuestionAnswermap(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.questionAnswerList = objectMapper.readValue(questionAnswer,
                    new TypeReference<List<QuestionAnswer>>() {});
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @PrePersist
    @PreUpdate
    private void saveQuestionAnswermap(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.questionAnswer = objectMapper.writeValueAsString(questionAnswerList);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}