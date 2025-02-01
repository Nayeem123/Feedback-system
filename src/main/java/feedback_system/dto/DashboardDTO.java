package feedback_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private String categoryName;
    private int openCount = 0;
    private int resolvedCount = 0;
    private int inProgressCount = 0;
    private Long feedbackId;
}
