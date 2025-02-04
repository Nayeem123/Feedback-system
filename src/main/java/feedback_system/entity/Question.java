package feedback_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isAnonymous;
    private String question;
    private String responseType;
    private Integer noOfOptions;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionData> optionsData;
    @ManyToOne
    @JoinColumn(name = "category_id") // Creates a foreign key column in 'questions' table
    private Category category;
}

