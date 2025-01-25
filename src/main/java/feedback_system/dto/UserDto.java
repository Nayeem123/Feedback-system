package feedback_system.dto;

import feedback_system.entity.Role;
import feedback_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private String gender;
    private String mobileNumber;
    private List<String> roles = new ArrayList<>();
    private String oldPassword;

    public static UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullname(user.getFullname());
        userDto.setUsername(user.getUsername());
        userDto.setGender(user.getGender());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    @Override
    public String toString() {
        return "UserDto [username=" + username + ", password=" + password + ", fullname=" + fullname + "]";
    }
}