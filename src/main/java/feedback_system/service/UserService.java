package feedback_system.service;


import feedback_system.dto.UserDto;
import feedback_system.entity.User;

public interface UserService {
    User findByUsername(String username);

    User save(UserDto userDto);

}