package feedback_system.dto;

import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDto {
    private Map<String,Object> payload;
}
