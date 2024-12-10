package feedback_system.utility;

import feedback_system.dto.UserDto;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean error;
    private String message;
    private UserDto user;
    private List<UserDto> userList;
}
