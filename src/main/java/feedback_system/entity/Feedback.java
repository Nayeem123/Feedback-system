package feedback_system.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
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
    @Transient
    private Map<String,String> questionAnswermap = new HashMap<>();

    @PostLoad
    private void loadQuestionAnswermap(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.questionAnswermap = objectMapper.readValue(questionAnswer,
                    new TypeReference<Map<String, String>>() {});
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @PrePersist
    @PreUpdate
    private void saveQuestionAnswermap(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.questionAnswer = objectMapper.writeValueAsString(questionAnswermap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}